package fr.lium.spkDiarization.libModel.ivector;

import java.util.logging.Logger;

import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import fr.lium.spkDiarization.lib.DiarizationException;
import fr.lium.spkDiarization.lib.SpkDiarizationLogger;
import fr.lium.spkDiarization.libMatrix.MatrixRowVector;
import fr.lium.spkDiarization.libMatrix.MatrixSquare;
import fr.lium.spkDiarization.libMatrix.MatrixSymmetric;
import fr.lium.spkDiarization.libModel.gaussian.FullGaussian;

/**
 * A factory for creating EigenFactorRadialNormalization objects.
 */
public class EigenFactorRadialNormalizationFactory {

	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(EigenFactorRadialNormalizationFactory.class.getName());

	/**
	 * Mean and covariance.
	 * 
	 * @param list the list
	 * @return the full gaussian
	 * @throws DiarizationException the diarization exception
	 */
	protected static FullGaussian meanAndCovariance(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list) throws DiarizationException {
		int size = list.get(0).getDimension();

		FullGaussian fg = new FullGaussian(size);
		fg.statistic_initialize();
		for (fr.lium.spkDiarization.libModel.ivector.IVector iVector : list) {
			fg.statistic_addFeature(iVector.data, 1.0);
		}
		// fg.statistic_debug();
		fg.statistic_setMeanAndCovariance();
		fg.statistic_reset();
		return fg;
	}

	/**
	 * Covariance.
	 * 
	 * @param list the list
	 * @return the matrix symmetric
	 * @throws DiarizationException the diarization exception
	 */
	static public MatrixSymmetric covariance(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list) throws DiarizationException {
		return meanAndCovariance(list).getCovariance();
	}

	/*
	 * protected static EigenFactorRadialData computeNormalization(IVectorArrayList list, EigenFactorRadialList normalization) throws DiarizationException { int size = list.get(0).getDimension(); FullGaussian statistic = meanAndCovariance(list);
	 * MatrixSquare cov = new MatrixSquare(size, size); for (int i = 0; i < size; i++) { for (int j = i; j < size; j++) { cov.set(i, j, statistic.getCovariance(i, j)); cov.set(j, i, statistic.getCovariance(j, i)); } } EigenvalueDecomposition eigen =
	 * cov.eig(); double[] eigenValues = eigen.getRealEigenvalues(); Matrix t = new Matrix(size, size, 0.0); Matrix eigenVectors = eigen.getV(); //t = eigenvalue^-1/2 * eigenVector for(int i = 0; i < size; i++) { double c = 1.0 /
	 * Math.sqrt(eigenValues[i]); for(int j = 0; j < size; j++) { t.set(i, j, c * eigenVectors.get(j, i)); } } return EigenFactorRadialList.createDataMeanCov(statistic, t); }
	 */

	/**
	 * Compute normalization.
	 * 
	 * @param list the list
	 * @param normalization the normalization
	 * @return the eigen factor radial data
	 * @throws DiarizationException the diarization exception
	 */
	protected static fr.lium.spkDiarization.libModel.ivector.EigenFactorRadialData computeNormalization(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list, fr.lium.spkDiarization.libModel.ivector.EigenFactorRadialList normalization) throws DiarizationException {
		int size = list.get(0).getDimension();
		FullGaussian statistic = meanAndCovariance(list);
// statistic.debug(10);

		SimpleMatrix cov = new SimpleMatrix(size, size);
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				cov.set(i, j, statistic.getCovariance(i, j));
				cov.set(j, i, statistic.getCovariance(j, i));
			}
		}

		@SuppressWarnings("rawtypes")
		SimpleEVD evd = cov.eig();

		MatrixSquare t = new MatrixSquare(size);
		// t = eigenvalue^1/2 * eigenVector
		for (int i = 0; i < size; i++) {
			double c = 1.0 / Math.sqrt(evd.getEigenvalue(i).real);
			for (int j = 0; j < size; j++) {
				t.set(i, j, c * evd.getEigenVector(i).get(j));
			}
		}
		return fr.lium.spkDiarization.libModel.ivector.EigenFactorRadialList.createDataMeanCov(statistic, t);
	}

	/**
	 * Applie.
	 * 
	 * @param list the list
	 * @param newList the new list
	 * @param mean the mean
	 * @param t the t
	 * @throws DiarizationException the diarization exception
	 */
	public static void applie(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list, fr.lium.spkDiarization.libModel.ivector.IVectorArrayList newList, MatrixRowVector mean, MatrixSquare t) throws DiarizationException {
		// debug OK
		for (fr.lium.spkDiarization.libModel.ivector.IVector iVector : list) {
			int size = iVector.getDimension();
			double diff[] = new double[size];
			fr.lium.spkDiarization.libModel.ivector.IVector newIVector = new IVector(iVector.getDimension(), iVector.getName(), iVector.getGender());
// logger.info("ivect no:" + iVector.getName());

			for (int i = 0; i < size; i++) {
				diff[i] = iVector.get(i) - mean.get(i);
			}
			double sum = 0.0;
			for (int i = 0; i < size; i++) {
				double value = 0.0;
				for (int j = 0; j < size; j++) {
					value += diff[j] * t.get(i, j);
				}
				newIVector.set(i, value);
				sum += value * value;
// logger.info("i: " + i + " = " + iVector.get(i)+" --> "+value);
			}
			double norm = Math.sqrt(sum);
// logger.info("ivect no:" + iVector.getName());
			for (int i = 0; i < size; i++) {
// double v = newIVector.get(i);
				newIVector.set(i, newIVector.get(i) / norm);
// logger.info("i: " + i + " = " + iVector.get(i)+" --> "+v+" / "+norm+" = "+newIVector.get(i));
			}
			newList.add(newIVector);
		}
	}

	/**
	 * Train.
	 * 
	 * @param list the list
	 * @param normalization the normalization
	 * @param nb the nb
	 * @return the i vector array list
	 * @throws DiarizationException the diarization exception
	 */
	public static fr.lium.spkDiarization.libModel.ivector.IVectorArrayList train(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list, fr.lium.spkDiarization.libModel.ivector.EigenFactorRadialList normalization, int nb) throws DiarizationException {
		fr.lium.spkDiarization.libModel.ivector.IVectorArrayList newList = null;
		for (int i = 0; i < nb; i++) {
			if (SpkDiarizationLogger.DEBUG) logger.info("InterSessionCompensation train it:" + i + " / " + nb);
			newList = new fr.lium.spkDiarization.libModel.ivector.IVectorArrayList(list.size());
			EigenFactorRadialData data = computeNormalization(list, normalization);
			normalization.add(data);
			applie(list, newList, data.getMean(), data.getT());
			list = newList;
		}
		normalization.setGlobalMeanCovariance(meanAndCovariance(newList));

		return newList;
	}

	/**
	 * Applied.
	 * 
	 * @param list the list
	 * @param normalization the normalization
	 * @return the i vector array list
	 * @throws DiarizationException the diarization exception
	 */
	public static fr.lium.spkDiarization.libModel.ivector.IVectorArrayList applied(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list, fr.lium.spkDiarization.libModel.ivector.EigenFactorRadialList normalization) throws DiarizationException {
		// debug OK
		return applied(list, normalization, normalization.size());
	}

	/**
	 * Applied.
	 * 
	 * @param list the list
	 * @param normalization the normalization
	 * @param nb the nb
	 * @return the i vector array list
	 * @throws DiarizationException the diarization exception
	 */
	public static fr.lium.spkDiarization.libModel.ivector.IVectorArrayList applied(fr.lium.spkDiarization.libModel.ivector.IVectorArrayList list, EigenFactorRadialList normalization, int nb) throws DiarizationException {
		// debug OK
		fr.lium.spkDiarization.libModel.ivector.IVectorArrayList newList = null;
		for (int i = 0; i < nb; i++) {
			if (SpkDiarizationLogger.DEBUG) logger.info("InterSessionCompensation applied it:" + i + " / " + nb);
			newList = new IVectorArrayList(list.size());
			applie(list, newList, normalization.getMean(i), normalization.getT(i));
			// newList.get(0).debug();
			list = newList;
		}
		return newList;
	}

}
