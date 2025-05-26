package es.gobcan.istac.indicators.rest.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import es.gobcan.istac.indicators.rest.enume.LabelVisualisationModeEnum;

public class IndicatorSelection {

    // We only support a max of 20 dimensions each type
    public static final int                                LEFT_DIMENSIONS_START_POSITION  = 0;
    public static final int                                TOP_DIMENSIONS_START_POSITION   = 20;
    public static final int                                FIXED_DIMENSIONS_START_POSITION = 40;

    private boolean                                        userSelection                   = true;

    private List<IndicatorSelectionDimension>              dimensions                      = new ArrayList<IndicatorSelectionDimension>();
    private final Map<String, IndicatorSelectionDimension> dimensionsMap                   = new HashMap<String, IndicatorSelectionDimension>();

    private List<IndicatorSelectionAttribute>              attributes                      = new ArrayList<IndicatorSelectionAttribute>();
    private final Map<String, IndicatorSelectionAttribute> attributesMap                   = new HashMap<String, IndicatorSelectionAttribute>();

    private final Map<String, Integer>                     multipliers                     = new HashMap<String, Integer>();

    public IndicatorSelection(List<IndicatorSelectionDimension> dimensions, List<IndicatorSelectionAttribute> attributes, boolean userSelection) {
        this(dimensions, attributes);
        this.userSelection = userSelection;
    }

    public IndicatorSelection(List<IndicatorSelectionDimension> dimensions, List<IndicatorSelectionAttribute> attributes) {
        if (dimensions != null) {
            this.dimensions = new ArrayList<IndicatorSelectionDimension>(dimensions);
            for (IndicatorSelectionDimension dimension : dimensions) {
                dimensionsMap.put(dimension.getId(), dimension);
            }
        }
        if (attributes != null) {
            this.attributes = new ArrayList<IndicatorSelectionAttribute>(attributes);
            for (IndicatorSelectionAttribute attribute : attributes) {
                attributesMap.put(attribute.getId(), attribute);
            }
        }

        recalculateMultipliers();
    }

    private void recalculateMultipliers() {
        initializeMultipliers(getLeftDimensions());
        initializeMultipliers(getTopDimensions());
        initializedFixedMultipliers();
    }

    public boolean isUserSelection() {
        return userSelection;
    }

    public IndicatorSelectionDimension getDimension(String dimensionId) {
        return dimensionsMap.get(dimensionId);
    }

    public List<IndicatorSelectionDimension> getDimensions() {
        return new ArrayList<IndicatorSelectionDimension>(dimensions);
    }

    public LabelVisualisationModeEnum getDimensionLabelVisualisationModel(String dimensionId) {
        IndicatorSelectionDimension dimension = getDimension(dimensionId);
        LabelVisualisationModeEnum labelVisualisationMode = dimension != null ? dimension.getLabelVisualisationMode() : null;
        return labelVisualisationMode;
    }

    public IndicatorSelectionAttribute getAttribute(String attributeId) {
        return attributesMap.get(attributeId);
    }

    public List<IndicatorSelectionAttribute> getAttributes() {
        return new ArrayList<IndicatorSelectionAttribute>(attributes);
    }

    public LabelVisualisationModeEnum getAttributeLabelVisualisationModel(String attributeId) {
        IndicatorSelectionAttribute attribute = getAttribute(attributeId);
        LabelVisualisationModeEnum labelVisualisationMode = attribute != null ? attribute.getLabelVisualisationMode() : null;
        return labelVisualisationMode;
    }

    private void initializeMultipliers(List<IndicatorSelectionDimension> dimensions) {
        ListIterator<IndicatorSelectionDimension> dimensionsListInterator = dimensions.listIterator(dimensions.size());
        int incrementCounter = 1;

        // Iterate the list in reverse order: right to left or down to up in the display table for calculate cell spacing
        while (dimensionsListInterator.hasPrevious()) {
            IndicatorSelectionDimension dimension = dimensionsListInterator.previous();
            multipliers.put(dimension.getId(), incrementCounter);
            incrementCounter *= dimension.getSelectedDimensionValues().size();
        }
    }

    private void initializedFixedMultipliers() {
        List<IndicatorSelectionDimension> fixedDimensions = getFixedDimensions();
        for (IndicatorSelectionDimension dimension : fixedDimensions) {
            multipliers.put(dimension.getId(), 1);
        }
    }

    /**
     * Calculate rows
     *
     * @return the number of total rows in the displayed data table
     */
    public int getRows() {
        List<IndicatorSelectionDimension> leftDimensions = getLeftDimensions();
        if (leftDimensions.size() > 0) {
            IndicatorSelectionDimension biggerLeftDimension = leftDimensions.get(0);
            return multipliers.get(biggerLeftDimension.getId()) * biggerLeftDimension.getSelectedDimensionValues().size();
        } else {
            return 1;
        }
    }

    /**
     * Calculate columns
     *
     * @return the number of total columns in the displayed data table
     */
    public int getColumns() {
        List<IndicatorSelectionDimension> topDimensions = getTopDimensions();
        if (topDimensions.size() > 0) {
            IndicatorSelectionDimension biggerTopDimension = topDimensions.get(0);
            return multipliers.get(biggerTopDimension.getId()) * biggerTopDimension.getSelectedDimensionValues().size();
        } else {
            return 1;
        }
    }

    public List<IndicatorSelectionDimension> getLeftDimensions() {
        return getDimensionsInPositionRange(LEFT_DIMENSIONS_START_POSITION, TOP_DIMENSIONS_START_POSITION);
    }

    public List<IndicatorSelectionDimension> getTopDimensions() {
        return getDimensionsInPositionRange(TOP_DIMENSIONS_START_POSITION, FIXED_DIMENSIONS_START_POSITION);
    }

    public List<IndicatorSelectionDimension> getFixedDimensions() {
        return getDimensionsInPositionRange(FIXED_DIMENSIONS_START_POSITION, FIXED_DIMENSIONS_START_POSITION + 20);
    }

    // FIXME:NO USAGE
    // public void moveTopDimensionsToLeft() {
    // int currentAvailablePosition = LEFT_DIMENSIONS_START_POSITION;
    // for (IndicatorSelectionDimension dimension : getDimensions()) {
    // // We are not interested on fixedDimensions
    // if (getFixedDimensions().contains(dimension)) {
    // continue;
    // }
    // dimension.setPosition(currentAvailablePosition);
    // currentAvailablePosition += 1;
    // }
    // recalculateMultipliers();
    // }

    private List<IndicatorSelectionDimension> getDimensionsInPositionRange(int from, int to) {
        List<IndicatorSelectionDimension> dimensionsInRange = new ArrayList<IndicatorSelectionDimension>();
        for (IndicatorSelectionDimension dimension : getDimensions()) {
            if (dimension.getPosition() >= from && dimension.getPosition() < to) {
                dimensionsInRange.add(dimension);
            }
        }
        sortDimensionsByPosition(dimensionsInRange);
        return dimensionsInRange;
    }

    private void sortDimensionsByPosition(List<IndicatorSelectionDimension> dimensions) {
        Collections.sort(dimensions, new Comparator<IndicatorSelectionDimension>() {

            @Override
            public int compare(IndicatorSelectionDimension indicatorSelectionDimension, IndicatorSelectionDimension indicatorSelectionDimension2) {
                return indicatorSelectionDimension.getPosition() - indicatorSelectionDimension2.getPosition(); // Ascending sort
            }
        });
    }

    /**
     * Calculate the key permutation for a cell of data
     *
     * @param row
     * @param column
     * @return
     */
    public Map<String, String> permutationAtCell(int row, int column) {
        Map<String, String> permutation = new HashMap<String, String>();

        for (IndicatorSelectionDimension dimension : getLeftDimensions()) {
            Integer multiplier = multipliers.get(dimension.getId());
            int selectedCategoryIndex = (row / multiplier) % dimension.getSelectedDimensionValues().size();
            permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(selectedCategoryIndex));
        }

        for (IndicatorSelectionDimension dimension : getTopDimensions()) {
            Integer multiplier = multipliers.get(dimension.getId());
            int selectedCategoryIndex = (column / multiplier) % dimension.getSelectedDimensionValues().size();
            permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(selectedCategoryIndex));
        }

        for (IndicatorSelectionDimension dimension : getFixedDimensions()) {
            if (dimension.getSelectedDimensionValues().size() > 0) {
                permutation.put(dimension.getId(), dimension.getSelectedDimensionValues().get(0));
            }
        }

        return permutation;
    }
    // FIXME: NO USAGE
    // public Map<String, String> permutationAtDimension(String dimensionId, String selectedDimensionValue) {
    // Map<String, String> permutation = new HashMap<>();
    // permutation.put(dimensionId, selectedDimensionValue);
    // return permutation;
    // }
    //
    // public int getMultiplierForDimension(IndicatorSelectionDimension dimension) {
    // return multipliers.get(dimension.getId());
    // }
}
