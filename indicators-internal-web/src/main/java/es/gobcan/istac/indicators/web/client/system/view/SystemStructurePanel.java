package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.web.client.enums.GeographicalSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.TimeSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.DimensionDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorInstanceDS;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;
import es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNode;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.widgets.GeographicalSelectItem;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemStructurePanel extends HLayout {

    private SystemUiHandler          uiHandlers;
    private IndicatorsSystemDtoWeb   system;

    private EditableTreePanel        treePanelEdit;

    private DimensionPanel           dimensionPanel;
    private IndicatorInstancePanel   indicatorInstPanel;

    private DeleteConfirmationWindow dimensionDeleteConfirm;
    private DeleteConfirmationWindow indInstanceDeleteConfirm;

    private DimensionDto             selectDimForCreate;
    private DimensionDto             selectedDimension;

    private IndicatorInstanceDto     selectedIndInstance;

    public SystemStructurePanel() {
        super();

        treePanelEdit = new EditableTreePanel();
        treePanelEdit.setMargin(15);

        dimensionPanel = new DimensionPanel();
        dimensionPanel.setVisibility(Visibility.HIDDEN);

        indicatorInstPanel = new IndicatorInstancePanel();
        indicatorInstPanel.setVisibility(Visibility.HIDDEN);

        dimensionDeleteConfirm = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemStrucDimDeleteConfirm());
        indInstanceDeleteConfirm = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemStrucIndInstanceDeleteConfirm());

        VLayout formLayout = new VLayout();
        formLayout.setMargin(15);
        formLayout.addMember(dimensionPanel);
        formLayout.addMember(indicatorInstPanel);

        this.addMember(treePanelEdit);
        this.addMember(formLayout);

        bindEvents();
    }

    private void bindEvents() {
        dimensionDeleteConfirm.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hidePanels();
                uiHandlers.deleteDimension(selectedDimension);
            }
        });
        indInstanceDeleteConfirm.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hidePanels();
                uiHandlers.deleteIndicatorInstance(selectedIndInstance);
            }
        });
    }

    public void setIndicatorsSystem(IndicatorsSystemDtoWeb indSys) {
        this.system = indSys;
        treePanelEdit.setIndicatorsSystem(indSys);
        dimensionPanel.setIndicatorsSystem(indSys);
        hidePanels();
    }

    public void setIndicators(List<IndicatorDto> indicators) {
        indicatorInstPanel.setIndicators(indicators);
    }

    public void setIndicatorFromIndicatorInstance(IndicatorDto indicator) {
        indicatorInstPanel.setIndicator(indicator);
    }

    public void setIndicatorSystemStructure(IndicatorsSystemDtoWeb indicatorsSystem, IndicatorsSystemStructureDto structure) {
        if (this.system != null && indicatorsSystem != null) {
            if (this.system.getCode().equals(indicatorsSystem.getCode())) { // reloading same structure
                // check if we have just persisted the system, in that case reload
                if (this.system.getUuid() == null && structure.getUuid() != null) {
                    uiHandlers.retrieveIndSystem(this.system.getCode());
                }
            } else { // loading new structure, we must hide panels with old information
                hidePanels();
            }
        }
        this.system = indicatorsSystem;
        treePanelEdit.setIndicatorSystemStructure(structure);
    }

    public void selectDimension(DimensionDto dim) {
        if (indicatorInstPanel.isVisible()) {
            indicatorInstPanel.hide();
        }
        selectedDimension = dim;
        dimensionPanel.setDimension(dim);
        dimensionPanel.show();
    }

    public void selectIndicatorInstance(IndicatorInstanceDto instance) {
        if (dimensionPanel.isVisible()) {
            dimensionPanel.hide();
        }
        selectedIndInstance = instance;
        indicatorInstPanel.setIndicatorInstance(instance);
        indicatorInstPanel.show();
    }

    private void showDimCreatePanel() {
        hidePanels();
        selectedDimension = null;
        selectedIndInstance = null;
        dimensionPanel.clearForms();
        dimensionPanel.setEditionMode();
        dimensionPanel.resetTitle();
        dimensionPanel.show();
    }

    private void showIndicInstanceCreatePanel() {
        hidePanels();
        // reload indicators
        uiHandlers.retrieveIndicators();
        selectedDimension = null;
        selectedIndInstance = null;
        indicatorInstPanel.clearForms();
        indicatorInstPanel.setEditionMode();
        indicatorInstPanel.resetTitle();
        indicatorInstPanel.show();
    }

    private void saveDimension(DimensionDto dim) {
        boolean creating = (selectedDimension == null);
        selectedDimension = dim;
        if (creating) {
            dim.setParentUuid(selectDimForCreate == null ? null : selectDimForCreate.getUuid()); // Whether is root's child or dimension's
            dim.setOrderInLevel(1L); // First child in parent
            uiHandlers.createDimension(system, dim);
        } else {
            uiHandlers.updateDimension(dim);
        }
    }

    private void saveIndicatorInstance(IndicatorInstanceDto indicatorInstanceDto) {
        boolean creating = (selectedIndInstance == null || selectedIndInstance.getUuid() == null);
        selectedIndInstance = indicatorInstanceDto;
        if (creating) {
            indicatorInstanceDto.setParentUuid(selectDimForCreate == null ? null : selectDimForCreate.getUuid()); // Whether is root's child or dimension's
            indicatorInstanceDto.setOrderInLevel(1L); // First child in parent
            uiHandlers.createIndicatorInstance(system, indicatorInstanceDto);
        } else {
            uiHandlers.updateIndicatorInstance(indicatorInstanceDto);
        }
    }

    public void onDimensionSaved(DimensionDto dimension) {
        selectDimension(dimension);
        dimensionPanel.onDimensionSaved(dimension);
    }

    public void onIndicatorInstanceSaved(IndicatorInstanceDto instance) {
        selectIndicatorInstance(instance);
        indicatorInstPanel.onIndicatorInstanceSaved(instance);
    }

    private void hidePanels() {
        // if (dimensionPanel.isVisible()) {
        dimensionPanel.hide();
        // }
        // if (indicatorInstPanel.isVisible()) {
        indicatorInstPanel.hide();
        // }
    }

    public void setUiHandlers(SystemUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setGeographicalGranularitiesForIndicator(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        indicatorInstPanel.setGeographicalGranularitiesForIndicator(geographicalGranularityDtos);
    }

    public void setGeographicalValuesForIndicatorForIndicator(List<GeographicalValueDto> geographicalValueDtos) {
        indicatorInstPanel.setGeographicalValues(geographicalValueDtos);
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        indicatorInstPanel.setGeographicalValue(geographicalValueDto);
    }

    public void onIndicatorDataPopulated(IndicatorDto indicatorDto) {
        indicatorInstPanel.onIndicatorDataPopulated(indicatorDto);
    }

    public void setTemporalGranularitiesForIndicator(List<TimeGranularityEnum> timeGranularityEnums) {
        indicatorInstPanel.setTemporalGranularitiesForIndicator(timeGranularityEnums);
    }

    public void setTemporalValuesFormIndicator(List<String> timeValues) {
        indicatorInstPanel.setTemporalValuesFormIndicator(timeValues);
    }

    private class TreePanel extends VLayout {

        protected IndicatorsSystemDtoWeb                     indicatorsSystemDtoWeb;

        protected final Long                                 FALSE_ROOT_NODE_ID = 0L;
        protected final Long                                 ROOT_NODE_ID       = 1L;

        /* TREE MANAGEMENT */
        protected IndSystemContentNode                       falseRoot;
        protected Tree                                       tree;
        protected final TreeGrid                             treeGrid;
        protected Map<ElementLevelDto, IndSystemContentNode> sourceMapping;
        protected String                                     treeOpenState;          // Internal representation that helps us to recover opened nodes after reloading tree

        public TreePanel() {
            super();
            this.setHeight(600);
            falseRoot = new IndSystemContentNode(FALSE_ROOT_NODE_ID.toString(), "", ROOT_NODE_ID.toString(), null);

            tree = new Tree();
            tree.setModelType(TreeModelType.PARENT);
            tree.setNameProperty(IndSystemContentNode.ATTR_NAME);
            tree.setIdField(IndSystemContentNode.ATTR_ID);
            tree.setParentIdField(IndSystemContentNode.ATTR_PARENT);
            tree.setRootValue(ROOT_NODE_ID.toString());

            treeGrid = new TreeGrid();
            treeGrid.setShowHeader(false);
            treeGrid.setFields(new TreeGridField("Name"));
            treeGrid.setData(tree);
            treeGrid.setShowCellContextMenus(true);
            treeGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE);
            sourceMapping = new HashMap<ElementLevelDto, IndSystemContentNode>();
            this.addMember(treeGrid);
        }

        /**
         * Builds TreeNodes based on
         * 
         * @param parentNode
         * @param dimensions
         * @param totalInstances
         */
        private void buildNodes(TreeNode parentNode, List<ElementLevelDto> elementsLevel) {
            String parentId = parentNode.getAttribute("ID");
            for (ElementLevelDto elemLevel : elementsLevel) {
                if (elemLevel.isElementTypeDimension()) {
                    DimensionDto dim = elemLevel.getDimension();
                    IndSystemContentNode node = new IndSystemContentNode(dim.getUuid(), InternationalStringUtils.getLocalisedString(dim.getTitle()), parentId, elemLevel);
                    tree.add(node, parentNode);
                    sourceMapping.put(elemLevel, node);
                    List<ElementLevelDto> children = buildChildrenList(elemLevel.getSubelements());
                    buildNodes(node, children);
                } else if (elemLevel.isElementTypeIndicatorInstance()) {
                    IndicatorInstanceDto indInst = elemLevel.getIndicatorInstance();
                    IndSystemContentNode node = new IndSystemContentNode(indInst.getUuid(), InternationalStringUtils.getLocalisedString(indInst.getTitle()), parentId, elemLevel);
                    tree.add(node, parentNode);
                    sourceMapping.put(elemLevel, node);
                }
            }
        }

        public void setIndicatorsSystem(IndicatorsSystemDtoWeb indSys) {
            this.indicatorsSystemDtoWeb = indSys;
            falseRoot.setAttribute("Name", getLocalisedString(indSys.getTitle()));
            falseRoot.setAttribute("Source", indSys);
            treeGrid.redraw();
        }

        public void setIndicatorSystemStructure(IndicatorsSystemStructureDto structure) {
            // Clear the tree
            tree.removeList(tree.getAllNodes());
            sourceMapping.clear();

            // Build nodes recursively
            tree.add(falseRoot, "/");
            if (structure != null) {
                List<ElementLevelDto> children = buildChildrenList(structure.getElements());
                buildNodes(falseRoot, children);
                recoverOpenState();
            }
            treeGrid.markForRedraw();
        }

        private void recoverOpenState() {
            // recover opened nodes
            if (treeOpenState != null) {
                treeGrid.setOpenState(treeOpenState);
            }
            TreeNode node = null;
            if (selectedDimension != null) {
                node = sourceMapping.get(selectedDimension);
            }
            if (selectedIndInstance != null) {
                node = sourceMapping.get(selectedIndInstance);
            }
            if (node != null) {
                TreeNode[] ascendantNodes = treeGrid.getData().getParents(node);
                treeGrid.getData().openFolders(ascendantNodes);
            }

        }

        /*
         * Given subdimensions and ALL indicatorInstances, the method must create a new List containing all parentUuid direct children
         * and it must be sorted by orderInLevel field.
         */
        private List<ElementLevelDto> buildChildrenList(List<ElementLevelDto> elementsLevel) {
            Collections.sort(elementsLevel, new Comparator<ElementLevelDto>() {

                @Override
                public int compare(ElementLevelDto o1, ElementLevelDto o2) {
                    return o1.getOrderInLevel().compareTo(o2.getOrderInLevel());
                }
            });
            return elementsLevel;
        }
    }

    private class EditableTreePanel extends TreePanel {

        public EditableTreePanel() {
            super();
            treeGrid.setLeaveScrollbarGap(false);
            treeGrid.setCanReorderRecords(true);
            treeGrid.setCanAcceptDroppedRecords(true);
            treeGrid.setCanDragRecordsOut(false);
            treeGrid.setDragDataAction(DragDataAction.MOVE);
            bindEvents();
        }

        private void bindEvents() {
            treeGrid.addFolderContextClickHandler(new FolderContextClickHandler() {

                @Override
                public void onFolderContextClick(FolderContextClickEvent event) {
                    if (ClientSecurityUtils.canEditStructure(EditableTreePanel.this.indicatorsSystemDtoWeb.getCode())) {
                        IndSystemContentNode node = (IndSystemContentNode) event.getFolder();
                        Menu menu = buildContextMenuNode(node);
                        menu.setAutoDraw(true);
                        menu.showContextMenu();
                        menu.setShowCellContextMenus(true);
                    }
                }
            });

            treeGrid.addLeafContextClickHandler(new LeafContextClickHandler() {

                @Override
                public void onLeafContextClick(LeafContextClickEvent event) {
                    if (ClientSecurityUtils.canEditStructure(EditableTreePanel.this.indicatorsSystemDtoWeb.getCode())) {
                        IndSystemContentNode node = (IndSystemContentNode) event.getLeaf();
                        Menu menu = buildContextMenuLeaf(node);
                        menu.showContextMenu();
                    }
                }
            });

            treeGrid.addFolderClickHandler(new FolderClickHandler() {

                @Override
                public void onFolderClick(FolderClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode) event.getFolder();
                    if (node.isDimension()) {
                        SystemStructurePanel.this.selectDimension(node.getSource().getDimension());
                    }
                }
            });
            treeGrid.addLeafClickHandler(new LeafClickHandler() {

                @Override
                public void onLeafClick(LeafClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode) event.getLeaf();
                    if (node.isIndicator()) {
                        SystemStructurePanel.this.selectIndicatorInstance(node.getSource().getIndicatorInstance());
                    }
                }
            });

            treeGrid.addFolderDropHandler(new FolderDropHandler() {

                @Override
                public void onFolderDrop(FolderDropEvent event) {
                    TreeNode dropFolder = event.getFolder();
                    TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                    int position = event.getIndex(); // Absolute position
                    if (isDroppable(dropFolder)) {
                        TreeNode[] siblings = treeGrid.getData().getChildren(dropFolder);

                        // We find out position of node under dropFolder
                        int relPosition = position; // Use to update position
                        int pos = -1;
                        for (int i = 0; i < siblings.length; i++) {
                            if (siblings[i] == droppedNode) {
                                pos = i;
                            }
                        }
                        if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                            relPosition--;
                        }

                        ElementLevelDto level = ((IndSystemContentNode) droppedNode).getSource();
                        // Get drop folder, finding out target dimension/root
                        IndSystemContentNode nodeParent = (IndSystemContentNode) (dropFolder);
                        String targetUuid = nodeParent.isRoot() ? null : nodeParent.getId();

                        Long order = relPosition + 1L; // Relative position starts at 0, order at 1

                        uiHandlers.moveSystemStructureNodes(system.getUuid(), targetUuid, level, order);
                    }
                    event.cancel();
                }
            });

            treeGrid.addFolderOpenedHandler(new FolderOpenedHandler() {

                // This method is used to save the open state
                @Override
                public void onFolderOpened(FolderOpenedEvent event) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                        @Override
                        public void execute() {
                            treeOpenState = treeGrid.getOpenState();
                        }
                    });
                }
            });

            treeGrid.addFolderClosedHandler(new FolderClosedHandler() {

                @Override
                public void onFolderClosed(FolderClosedEvent event) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                        @Override
                        public void execute() {
                            treeOpenState = treeGrid.getOpenState();
                        }
                    });
                }
            });
        }

        private boolean isDroppable(TreeNode dropFolder) {
            if (treeGrid.getDropFolder().getAttribute(IndSystemContentNode.ATTR_NAME).equals("/")) { // Replacing false root is not allowed
                return false;
            }
            return true;
        }

        private Menu buildContextMenuNode(IndSystemContentNode node) {
            final IndSystemContentNode selNode = node;
            Menu menu = new Menu();
            MenuItem item3 = new MenuItem(getConstants().systemStrucNewDim());
            item3.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus())) {
                        // If system is PUBLISH or ARCHIVED, dimension cannot be created
                        final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                        window.show();
                    } else {
                        // Create a new dimension
                        if (selNode == falseRoot)
                            SystemStructurePanel.this.selectDimForCreate = null;
                        else {
                            SystemStructurePanel.this.selectDimForCreate = selNode.getSource().getDimension();
                        }
                        SystemStructurePanel.this.showDimCreatePanel();
                    }
                }
            });
            MenuItem item4 = new MenuItem(getConstants().systemStrucNewIndInstance());
            item4.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus())) {
                        // If system is PUBLISH or ARCHIVED, dimension cannot be created
                        final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                        window.show();
                    } else {
                        // Create a new indicator instance
                        if (selNode == falseRoot)
                            SystemStructurePanel.this.selectDimForCreate = null;
                        else {
                            SystemStructurePanel.this.selectDimForCreate = selNode.getSource().getDimension();
                        }
                        SystemStructurePanel.this.showIndicInstanceCreatePanel();
                    }
                }
            });

            if (node.isDimension()) {
                MenuItem item1 = new MenuItem(getConstants().systemStrucDimDelete());
                item1.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(MenuItemClickEvent event) {
                        if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus())) {
                            // If system is PUBLISH or ARCHIVED, cannot be deleted
                            final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                            window.show();
                        } else {
                            DimensionDto dim = selNode.getSource().getDimension();
                            selectedDimension = dim;
                            dimensionDeleteConfirm.show();
                        }
                    }
                });
                menu.addItem(item1);
            }

            menu.addItem(item3);
            menu.addItem(item4);
            return menu;
        }

        private Menu buildContextMenuLeaf(IndSystemContentNode node) {
            final IndSystemContentNode selNode = node;
            Menu menu = new Menu();
            MenuItem item1 = new MenuItem(getConstants().systemStrucIndInstanceDelete());
            item1.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus())) {
                        // If system is PUBLISH or ARCHIVED, cannot be deleted
                        final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                        window.show();
                    } else {
                        IndicatorInstanceDto instance = selNode.getSource().getIndicatorInstance();
                        selectedIndInstance = instance;
                        indInstanceDeleteConfirm.show();
                    }
                }
            });
            menu.addItem(item1);
            return menu;
        }

    }

    private class DimensionPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            form;
        private GroupDynamicForm            editForm;

        private boolean                     createMode;

        public DimensionPanel() {
            mainFormLayout = new InternationalMainFormLayout();
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucDimTitle());
            mainFormLayout.setMargin(0);

            createViewForm();
            createEditForm();

            this.addMember(mainFormLayout);
            // Init
            createMode = false;
            bindEvents();
        }
        
        public void setIndicatorsSystem(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb) {
            // Show/hide edit button depending on the selected indicators system
            mainFormLayout.getEditToolStripButton().setVisibility(Visibility.HIDDEN);
            mainFormLayout.setCanEdit(ClientSecurityUtils.canEditDimension(indicatorsSystemDtoWeb.getCode()));
            mainFormLayout.setViewMode();
        }

        public void onDimensionSaved(DimensionDto dimensionDto) {
            mainFormLayout.setViewMode();
            setDimension(dimensionDto);
        }

        private void bindEvents() {
            // Remove handler from edit button
            mainFormLayout.getEditHandlerRegistration().removeHandler();
            mainFormLayout.getEditToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.ARCHIVED.equals(system.getProcStatus())) {
                        // Create a new version of the indicator
                        final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                        window.show();
                    } else {
                        setEditionMode();
                    }
                }
            });

            mainFormLayout.getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (editForm.validate(false)) {
                        DimensionDto dim = null;
                        if (selectedDimension != null) {
                            dim = fillDimension(selectedDimension);
                        } else {
                            dim = fillDimension(new DimensionDto());
                        }
                        SystemStructurePanel.this.saveDimension(dim);
                    }
                }
            });
            mainFormLayout.getTranslateToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    form.setTranslationsShowed(translationsShowed);
                    editForm.setTranslationsShowed(translationsShowed);
                }
            });
            mainFormLayout.getCancelToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (createMode) {
                        DimensionPanel.this.hide();
                    }
                }
            });
        }

        public void setDimension(DimensionDto dimensionDto) {
            createMode = dimensionDto.getUuid() == null;
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucDimTitle() + ": " + InternationalStringUtils.getLocalisedString(dimensionDto.getTitle()));
            setDimensionView(dimensionDto);
            setDimensionEdit(dimensionDto);
            this.markForRedraw();
        }

        public void resetTitle() {
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucDimTitle());
        }

        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        public void clearForms() {
            createMode = true;
            form.clearValues();
            editForm.clearValues();
        }

        private DimensionDto fillDimension(DimensionDto dim) {
            dim.setTitle((InternationalStringDto) (editForm.getValue(DimensionDS.TITLE)));
            return dim;
        }

        private void setDimensionView(DimensionDto dim) {
            form.setValue(DimensionDS.TITLE, RecordUtils.getInternationalStringRecord(dim.getTitle()));
        }

        private void setDimensionEdit(DimensionDto dim) {
            editForm.setValue(DimensionDS.TITLE, RecordUtils.getInternationalStringRecord(dim.getTitle()));
        }

        private void createViewForm() {
            form = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(DimensionDS.TITLE, getConstants().systemStrucDimName());
            form.setFields(name);
            mainFormLayout.addViewCanvas(form);
        }

        private void createEditForm() {
            editForm = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            MultiLanguageTextItem name = new MultiLanguageTextItem(DimensionDS.TITLE, getConstants().systemStrucDimName());
            name.setRequired(true);
            editForm.setFields(name);
            mainFormLayout.addEditionCanvas(editForm);
        }
    }

    private class IndicatorInstancePanel extends VLayout {

        private InternationalMainFormLayout      mainFormLayout;
        private GroupDynamicForm                 form;
        private GroupDynamicForm                 editionForm;
        private boolean                          createMode;

        private List<GeographicalGranularityDto> geographicalGranularityDtos;

        private List<IndicatorDto>               indicatorDtos;

        public IndicatorInstancePanel() {
            mainFormLayout = new InternationalMainFormLayout(false); // Indicator instances can never be modified. If not: new
                                                                     // InternationalMainFormLayout(ClientSecurityUtils.canEditIndicatorInstace());
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucIndInstanceTitle());
            mainFormLayout.setMargin(0);

            createViewForm();
            createEditForm();

            this.addMember(mainFormLayout);

            createMode = false;
            bindEvents();
        }

        public void onIndicatorInstanceSaved(IndicatorInstanceDto indicatorInstance) {
            mainFormLayout.setViewMode();
            setIndicatorInstance(indicatorInstance);
        }

        private void bindEvents() {
            // Remove handler from edit button
            mainFormLayout.getEditHandlerRegistration().removeHandler();
            mainFormLayout.getEditToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(system.getProcStatus()) || IndicatorsSystemProcStatusEnum.ARCHIVED.equals(system.getProcStatus())) {
                        final InformationWindow window = new InformationWindow(getMessages().systemEditionInfo(), getMessages().systemEditionInfoDetailedMessage());
                        window.show();
                    } else {
                        setEditionMode();
                    }
                }
            });

            mainFormLayout.getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (editionForm.validate(false)) {
                        IndicatorInstanceDto indicatorInstanceDto = null;
                        if (selectedIndInstance != null) {
                            indicatorInstanceDto = fillIndicatorInstance(selectedIndInstance);
                        } else {
                            indicatorInstanceDto = fillIndicatorInstance(new IndicatorInstanceDto());
                        }
                        SystemStructurePanel.this.saveIndicatorInstance(indicatorInstanceDto);
                    }
                }
            });

            mainFormLayout.getTranslateToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    form.setTranslationsShowed(translationsShowed);
                    editionForm.setTranslationsShowed(translationsShowed);
                }
            });
            mainFormLayout.getCancelToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (createMode) {
                        IndicatorInstancePanel.this.hide();
                    }
                }
            });
        }

        public void resetTitle() {
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucIndInstanceTitle());
        }

        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        public void clearForms() {
            createMode = true;
            form.clearValues();
            editionForm.clearValues();

            // Clear icons
            editionForm.getItem(IndicatorInstanceDS.IND_UUID).setShowIcons(Boolean.FALSE);

            // Clear temporal and geographical valueMaps
            editionForm.getItem(IndicatorInstanceDS.TIME_GRANULARITY).setValueMap(new LinkedHashMap<String, String>());
            editionForm.getItem(IndicatorInstanceDS.TIME_VALUE).setValueMap(new LinkedHashMap<String, String>());
            editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY).setValueMap(new LinkedHashMap<String, String>());
            ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).getGeoGranularitySelectItem().setValueMap(new LinkedHashMap<String, String>());
            ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).getGeoValueSelectItem().setValueMap(new LinkedHashMap<String, String>());
        }

        private IndicatorInstanceDto fillIndicatorInstance(IndicatorInstanceDto indicatorInstanceDto) {
            indicatorInstanceDto.setTitle((InternationalStringDto) (editionForm.getValue(IndicatorInstanceDS.TITLE)));
            indicatorInstanceDto.setIndicatorUuid(CommonUtils.getUuidString(editionForm.getValueAsString(IndicatorInstanceDS.IND_UUID)));
            indicatorInstanceDto.setTimeGranularity(editionForm.getItem(IndicatorInstanceDS.TIME_GRANULARITY).isVisible() ? TimeGranularityEnum.valueOf(editionForm
                    .getValueAsString(IndicatorInstanceDS.TIME_GRANULARITY)) : null);
            indicatorInstanceDto.setTimeValue(editionForm.getItem(IndicatorInstanceDS.TIME_VALUE).isVisible() ? editionForm.getValueAsString(IndicatorInstanceDS.TIME_VALUE) : null);
            indicatorInstanceDto.setGeographicalGranularityUuid(editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY).isVisible() ? CommonUtils.getUuidString(editionForm
                    .getValueAsString(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY)) : null);
            indicatorInstanceDto.setGeographicalValueUuid(editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE).isVisible() ? CommonUtils.getUuidString(((GeographicalSelectItem) editionForm
                    .getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).getSelectedGeoValue()) : null);
            return indicatorInstanceDto;
        }

        public void setIndicatorInstance(IndicatorInstanceDto indInst) {
            createMode = indInst.getUuid() == null;
            mainFormLayout.setTitleLabelContents(getConstants().systemStrucIndInstanceTitle() + ": " + InternationalStringUtils.getLocalisedString(indInst.getTitle()));
            // We need indicators in order to fill select item
            uiHandlers.retrieveIndicators();
            setIndicatorInstanceView(indInst);
            // Indicator instance edition not allowed
        }

        public void setIndicators(List<IndicatorDto> indicators) {
            this.indicatorDtos = indicators;
            LinkedHashMap<String, String> indicatorsMap = CommonUtils.getIndicatorsValueMap(indicators);
            ((SelectItem) editionForm.getItem(IndicatorInstanceDS.IND_UUID)).setValueMap(indicatorsMap);
        }

        public void setGeographicalGranularitiesForIndicator(List<GeographicalGranularityDto> geographicalGranularityDtos) {
            this.geographicalGranularityDtos = geographicalGranularityDtos;
            LinkedHashMap<String, String> valueMap = CommonUtils.getGeographicalGranularituesValueMap(geographicalGranularityDtos);
            ((SelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY)).setValueMap(CommonUtils.getGeographicalGranularituesValueMap(geographicalGranularityDtos));
            ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).setGeoGranularitiesValueMap(valueMap);
        }

        public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
            ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
        }

        public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
            // View form
            form.setValue(IndicatorInstanceDS.GEOGRAPHICAL_VALUE,
                    geographicalValueDto != null ? geographicalValueDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()) : new String());
            // Edition form (EDITION NOT ALLOWED)
            // if (geographicalValueDto != null) {
            // ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).setGeoGranularity(geographicalValueDto.getGranularityUuid());
            // // Make sure value map is set properly
            // uiHandlers.retrieveGeographicalValues(geographicalValueDto.getGranularityUuid());
            // }
        }

        private void setIndicator(IndicatorDto indicator) {
            form.setValue(IndicatorInstanceDS.IND_TITLE, indicator.getCode() + " - " + getLocalisedString(indicator.getTitle()));
        }

        private void setIndicatorInstanceView(IndicatorInstanceDto indInst) {
            form.setValue(IndicatorInstanceDS.TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));

            form.setValue(IndicatorInstanceDS.IND_TITLE, new String()); // Indicator title set in setIndicator method
            uiHandlers.retrieveIndicator(indInst.getIndicatorUuid());

            form.setValue(IndicatorInstanceDS.TIME_SELECTION_TYPE, getTimeSelectionTypeEnum(indInst) != null ? getTimeSelectionTypeEnum(indInst).toString() : "");
            form.setValue(IndicatorInstanceDS.TIME_SELECTION_TYPE + "-text", getTimeSelectionType(indInst));
            form.setValue(IndicatorInstanceDS.TIME_GRANULARITY,
                    indInst.getTimeGranularity() != null ? (getCoreMessages().getString(getCoreMessages().timeGranularityEnum() + indInst.getTimeGranularity().getName())) : "");
            form.setValue(IndicatorInstanceDS.TIME_VALUE, indInst.getTimeValue());

            form.setValue(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE, getGeographicalSelectionTypeEnum(indInst) != null ? getGeographicalSelectionTypeEnum(indInst).toString() : "");
            form.setValue(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE + "-text", getGeoSelectionType(indInst));
            form.setValue(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY, getGeographicalGranularityTitle(indInst.getGeographicalGranularityUuid()));

            // Geographical value set in setGeographicalValue method
            form.setValue(IndicatorInstanceDS.GEOGRAPHICAL_VALUE, new String());
            if (indInst.getGeographicalValueUuid() != null && !indInst.getGeographicalValueUuid().isEmpty()) {
                uiHandlers.retrieveGeographicalValue(indInst.getGeographicalValueUuid());
            }

            form.markForRedraw();
        }
        // NO EDITABLE: If edition were allowed, should be a two steps edition or something like that (similar to data sources edition)
        // private void setIndicatorInstanceEdit(IndicatorInstanceDto indInst) {
        // editionForm.setValue(IndicatorInstanceDS.TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));
        // editionForm.setValue(IndicatorInstanceDS.IND_UUID, indInst.getIndicatorUuid());
        //
        // editionForm.setValue(IndicatorInstanceDS.TIME_SELECTION_TYPE, getTimeSelectionTypeEnum(indInst) != null ? getTimeSelectionTypeEnum(indInst).toString() : "");
        // editionForm.setValue(IndicatorInstanceDS.TIME_GRANULARITY, indInst.getTimeGranularity() != null ? indInst.getTimeGranularity().toString() : "");
        // editionForm.setValue(IndicatorInstanceDS.TIME_VALUE, indInst.getTimeValue());
        //
        // editionForm.setValue(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE, getGeographicalSelectionTypeEnum(indInst) != null ? getGeographicalSelectionTypeEnum(indInst).toString() : "");
        // editionForm.setValue(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY, indInst.getGeographicalGranularityUuid());
        // ((GeographicalSelectItem) editionForm.getItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE)).setGeoValue(indInst.getGeographicalValueUuid());
        //
        // markForRedraw();
        // }

        private void createViewForm() {
            form = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());

            ViewMultiLanguageTextItem name = new ViewMultiLanguageTextItem(IndicatorInstanceDS.TITLE, getConstants().systemStrucIndInstanceTitleField());

            ViewTextItem indicatorNameItem = new ViewTextItem(IndicatorInstanceDS.IND_TITLE, getConstants().systemStrucIndInstanceIndicator());

            // Time

            ViewTextItem timeSelection = new ViewTextItem(IndicatorInstanceDS.TIME_SELECTION_TYPE, getConstants().instanceTimeSelection());
            timeSelection.setVisible(false);

            ViewTextItem timeSelectionText = new ViewTextItem(IndicatorInstanceDS.TIME_SELECTION_TYPE + "-text", getConstants().instanceTimeSelection());

            ViewTextItem timeGranularityItem = new ViewTextItem(IndicatorInstanceDS.TIME_GRANULARITY, getConstants().instanceTimeGranularity());
            timeGranularityItem.setShowIfCondition(getTimeGranularityIfFunction());

            ViewTextItem timeValue = new ViewTextItem(IndicatorInstanceDS.TIME_VALUE, getConstants().instanceTimeValue());
            timeValue.setShowIfCondition(getTimeValueIfFunction());

            // Geographic

            ViewTextItem geographicSelection = new ViewTextItem(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE, getConstants().instanceGeographicalSelection());
            geographicSelection.setVisible(false);

            ViewTextItem geographicSelectionText = new ViewTextItem(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE + "-text", getConstants().instanceGeographicalSelection());

            ViewTextItem geoGranularity = new ViewTextItem(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY, getConstants().instanceGeographicalGranularity());
            geoGranularity.setShowIfCondition(getGeoGranularityIfFunction());

            ViewTextItem geoValue = new ViewTextItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE, getConstants().instanceGeographicalValue());
            geoValue.setShowIfCondition(getGeoValueIfFunction());

            form.setFields(name, indicatorNameItem, timeSelection, timeSelectionText, timeGranularityItem, timeValue, geographicSelection, geographicSelectionText, geoGranularity, geoValue);
            mainFormLayout.addViewCanvas(form);
        }

        private void createEditForm() {
            editionForm = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());

            // Name

            MultiLanguageTextItem name = new MultiLanguageTextItem(IndicatorInstanceDS.TITLE, getConstants().systemStrucIndInstanceTitleField());
            name.setRequired(true);

            // INDICATOR

            final RequiredSelectItem indicatorsItem = new RequiredSelectItem(IndicatorInstanceDS.IND_UUID, getConstants().systemStrucIndInstanceIndicator());
            indicatorsItem.setShowIcons(false);
            indicatorsItem.addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    indicatorsItem.setShowIcons(Boolean.FALSE); // Hide warning population data icon
                    if (event.getValue() != null && !StringUtils.isBlank(event.getValue().toString())) {
                        IndicatorDto indicatorDto = getIndicatorDtoByUuid(event.getValue().toString());
                        if (indicatorDto != null) {
                            // Check if it is necessary to populate indicators data
                            if (indicatorDto.needsBePopulated()) {
                                indicatorsItem.setShowIcons(Boolean.TRUE);
                            }
                            // Load indicators temporal and geographical granularities and values
                            uiHandlers.retrieveTimeGranularitiesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                            uiHandlers.retrieveTimeValuesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                            uiHandlers.retrieveGeographicalGranularitiesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                        }
                    }
                    editionForm.markForRedraw();

                }
            });
            // Warning icon
            FormItemIcon infoIcon = new FormItemIcon();
            infoIcon.setSrc(GlobalResources.RESOURCE.warn().getURL());
            infoIcon.setPrompt(getMessages().indicatorInconsistentData());
            // Populate data icon
            FormItemIcon populateData = new FormItemIcon();
            populateData.setSrc(IndicatorsResources.RESOURCE.populateData().getURL());
            populateData.setPrompt(getConstants().indicatorPopulateData());
            indicatorsItem.setIcons(infoIcon, populateData);
            populateData.addFormItemClickHandler(new FormItemClickHandler() {

                @Override
                public void onFormItemClick(FormItemIconClickEvent event) {
                    if (event.getItem().getValue() != null) {
                        String indicatorUuid = event.getItem().getValue().toString();
                        if (!StringUtils.isBlank(indicatorUuid)) {
                            IndicatorDto indicatorDto = getIndicatorDtoByUuid(indicatorUuid);
                            if (indicatorDto != null) {
                                uiHandlers.populateIndicatorData(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                            }
                        }
                    }
                }
            });

            // Time

            RequiredSelectItem timeSelectionType = new RequiredSelectItem(IndicatorInstanceDS.TIME_SELECTION_TYPE, getConstants().instanceTimeSelection());
            timeSelectionType.setValueMap(CommonUtils.getTimeSelectionTypeMap());
            timeSelectionType.addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    editionForm.markForRedraw();
                }
            });

            RequiredSelectItem timeGranularityItem = new RequiredSelectItem(IndicatorInstanceDS.TIME_GRANULARITY, getConstants().instanceTimeGranularity());
            timeGranularityItem.setShowIfCondition(getTimeGranularityIfFunction()); // valueMap set in setTemporalGranularitiesForIndicator

            RequiredSelectItem timeValue = new RequiredSelectItem(IndicatorInstanceDS.TIME_VALUE, getConstants().instanceTimeValue());
            timeValue.setShowIfCondition(getTimeValueIfFunction()); // valueMap set in setTemporalValuesForIndicator

            // Geographical

            SelectItem geographicalSelectionType = new SelectItem(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE, getConstants().instanceGeographicalSelection());
            geographicalSelectionType.setValueMap(CommonUtils.getGeographicalSelectionTypeValueMap());
            geographicalSelectionType.addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    editionForm.markForRedraw();
                }
            });

            RequiredSelectItem geographicalGranularity = new RequiredSelectItem(IndicatorInstanceDS.GEOGRAPHICAL_GRANULARITY, getConstants().instanceGeographicalGranularity());
            geographicalGranularity.setShowIfCondition(getGeoGranularityIfFunction());

            final GeographicalSelectItem geographicalValue = new GeographicalSelectItem(IndicatorInstanceDS.GEOGRAPHICAL_VALUE, getConstants().instanceGeographicalValue());
            geographicalValue.setRequired(true);
            geographicalValue.setShowIfCondition(getGeoValueIfFunction());
            geographicalValue.getGeoGranularitySelectItem().addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    // Clear geographical value
                    geographicalValue.setGeoValuesValueMap(new LinkedHashMap<String, String>());
                    geographicalValue.setGeoValue(new String());
                    // Set values with selected granularity
                    if (event.getValue() != null && !StringUtils.isBlank(event.getValue().toString())) {
                        String geographicalGranularityUuid = event.getValue().toString();
                        if (editionForm.getValue(IndicatorInstanceDS.IND_UUID) != null && !StringUtils.isBlank(editionForm.getValue(IndicatorInstanceDS.IND_UUID).toString())) {
                            IndicatorDto indicatorDto = getIndicatorDtoByUuid(editionForm.getValue(IndicatorInstanceDS.IND_UUID).toString());
                            if (indicatorDto != null) {
                                uiHandlers.retrieveGeographicalValuesWithGranularityInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber(), geographicalGranularityUuid);
                            }
                        }
                    }
                }
            });

            editionForm.setFields(name, indicatorsItem, timeSelectionType, timeGranularityItem, timeValue, geographicalSelectionType, geographicalGranularity, geographicalValue);
            mainFormLayout.addEditionCanvas(editionForm);
        }
        // Time functions

        private FormItemIfFunction getTimeGranularityIfFunction() {
            return new FormItemIfFunction() {

                @Override
                public boolean execute(FormItem item, Object value, DynamicForm form) {
                    String type = form.getValueAsString(IndicatorInstanceDS.TIME_SELECTION_TYPE);
                    return type != null && TimeSelectionTypeEnum.GRANULARITY.toString().equals(type);
                }
            };
        }

        private FormItemIfFunction getTimeValueIfFunction() {
            return new FormItemIfFunction() {

                @Override
                public boolean execute(FormItem item, Object value, DynamicForm form) {
                    String type = form.getValueAsString(IndicatorInstanceDS.TIME_SELECTION_TYPE);
                    return type != null && TimeSelectionTypeEnum.VALUE.toString().equals(type);
                }
            };
        }

        private String getTimeSelectionType(IndicatorInstanceDto indicatorInstanceDto) {
            TimeSelectionTypeEnum type = getTimeSelectionTypeEnum(indicatorInstanceDto);
            if (type != null) {
                return getCoreMessages().getString(getCoreMessages().timeSelectionTypeEnum() + type.getName());
            }
            return new String();
        }

        private TimeSelectionTypeEnum getTimeSelectionTypeEnum(IndicatorInstanceDto indicatorInstanceDto) {
            if (indicatorInstanceDto.getTimeValue() != null && !indicatorInstanceDto.getTimeValue().isEmpty()) {
                return TimeSelectionTypeEnum.VALUE;
            } else if (indicatorInstanceDto.getTimeGranularity() != null) {
                return TimeSelectionTypeEnum.GRANULARITY;
            }
            return null;
        }

        // Geographical functions

        private FormItemIfFunction getGeoGranularityIfFunction() {
            return new FormItemIfFunction() {

                @Override
                public boolean execute(FormItem item, Object value, DynamicForm form) {
                    String type = form.getValueAsString(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE);
                    return type != null && GeographicalSelectionTypeEnum.GRANULARITY.toString().equals(type);
                }
            };
        }

        private FormItemIfFunction getGeoValueIfFunction() {
            return new FormItemIfFunction() {

                @Override
                public boolean execute(FormItem item, Object value, DynamicForm form) {
                    String type = form.getValueAsString(IndicatorInstanceDS.GEOGRAPHICAL_SELECTION_TYPE);
                    return type != null && GeographicalSelectionTypeEnum.VALUE.toString().equals(type);
                }
            };
        }

        private String getGeoSelectionType(IndicatorInstanceDto indicatorInstanceDto) {
            GeographicalSelectionTypeEnum type = getGeographicalSelectionTypeEnum(indicatorInstanceDto);
            if (type != null) {
                return getCoreMessages().getString(getCoreMessages().geographicalSelectionTypeEnum() + type.getName());
            }
            return new String();
        }

        private GeographicalSelectionTypeEnum getGeographicalSelectionTypeEnum(IndicatorInstanceDto indicatorInstanceDto) {
            if (indicatorInstanceDto.getGeographicalGranularityUuid() != null && !indicatorInstanceDto.getGeographicalGranularityUuid().isEmpty()) {
                return GeographicalSelectionTypeEnum.GRANULARITY;
            } else if (indicatorInstanceDto.getGeographicalValueUuid() != null && !indicatorInstanceDto.getGeographicalValueUuid().isEmpty()) {
                return GeographicalSelectionTypeEnum.VALUE;
            }
            return null;
        }

        private String getGeographicalGranularityTitle(String uuid) {
            if (uuid != null && !uuid.isEmpty()) {
                for (GeographicalGranularityDto dto : geographicalGranularityDtos) {
                    if (uuid.equals(dto.getUuid())) {
                        return InternationalStringUtils.getLocalisedString(dto.getTitle());
                    }
                }
            }
            return new String();
        }

        private IndicatorDto getIndicatorDtoByUuid(String uuid) {
            for (IndicatorDto indicatorDto : indicatorDtos) {
                if (indicatorDto.getUuid().equals(uuid)) {
                    return indicatorDto;
                }
            }
            return null;
        }

        public void onIndicatorDataPopulated(IndicatorDto indicatorDto) {
            // Hide warning and populate data icon
            editionForm.getItem(IndicatorInstanceDS.IND_UUID).setShowIcons(false);
            editionForm.markForRedraw();

            // Replace indicator in indicatorDtos
            int indexToRemove = -1;
            for (int i = 0; i < indicatorDtos.size(); i++) {
                if (indicatorDto.getUuid().equals(indicatorDtos.get(i).getUuid())) {
                    indexToRemove = i;
                    break;
                }
            }
            indicatorDtos.remove(indexToRemove);
            indicatorDtos.add(indicatorDto);

            // Reload temporal and geographical granularities and values
            uiHandlers.retrieveTimeGranularitiesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
            uiHandlers.retrieveTimeValuesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
            uiHandlers.retrieveGeographicalGranularitiesInIndicator(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
        }

        public void setTemporalGranularitiesForIndicator(List<TimeGranularityEnum> timeGranularityEnums) {
            editionForm.getItem(IndicatorInstanceDS.TIME_GRANULARITY).setValueMap(CommonUtils.getTimeGranularityValueMap(timeGranularityEnums));
        }

        public void setTemporalValuesFormIndicator(List<String> timeValues) {
            editionForm.getItem(IndicatorInstanceDS.TIME_VALUE).setValueMap(CommonUtils.getTimeValueValueMap(timeValues));
        }

    }

}
