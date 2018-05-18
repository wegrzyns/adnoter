package cea.evaluation.measure;

import cea.Util.ConfigurationUtil;
import cea.evaluation.model.SlideTransition;
import cea.evaluation.model.SlideTransitionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SlideTransitionTypeFilter {

    private static final String slideTransitionTypes;
    private static final List<SlideTransitionType> SLIDE_TRANSITION_TYPES;

    static {
        slideTransitionTypes = ConfigurationUtil.configuration().getString("evaluation.slideTransitionTypes");
        SLIDE_TRANSITION_TYPES = new ArrayList<>();
        Stream.of(slideTransitionTypes.split(",")).forEach(
                slideTransitionType -> SLIDE_TRANSITION_TYPES.add(SlideTransitionType.valueOf(slideTransitionType.trim())));
    }

    public static boolean filterSlideTransitions(SlideTransition slideTransition) {
        return SLIDE_TRANSITION_TYPES.contains(slideTransition.getType());
    }
}
