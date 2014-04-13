package TopDownShooter.MapEditor;
import TopDownShooter.MapSystem.TileCategory;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class CategoryPanel extends JPanel {
    private SelectionModel selection;
    
    JList<TileCategory> categoryList;
    
    public CategoryPanel() {
        selection = SelectionModel.getInstance();
        
        loadTileCategories();
    }
    
    private void loadTileCategories() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        
        //Shared constraints
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5,5,0,0);
        
        //JLabel
        constraints.gridy = 0;
        JLabel titleLabel = new JLabel("Tile Category:");
        this.add(titleLabel, constraints);
        
        //JSeperator
        constraints.gridy = 1;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        
        //JTree
        constraints.gridy = 2;
        constraints.weighty = 0.5;       
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode();
        fillNodes(topNode);
        JTree categoryTree = new JTree(topNode);
        categoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        categoryTree.setRootVisible(false);
        addTreeSelectionListener(categoryTree);
        JScrollPane categoryScrollPane = new JScrollPane(categoryTree);
        this.add(categoryScrollPane, constraints);
        
        //JSeperator
        constraints.gridy = 3;
        constraints.weighty = 0;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);
        
        //LayerSelectorPanel
        constraints.gridy = 4;
        LayerSelectorPanel layerPanel = new LayerSelectorPanel();
        this.add(layerPanel, constraints);
    }
    
    private void fillNodes(DefaultMutableTreeNode top) {
        /*
         * The heirarchy of nodes mirrors the heirarchy present in SelectionModel.
         * Each SelectionType contains zero or more sub-Categories (represented by a unique set of Enums)
         * An example would be that the MapTile SelectionType would be a typeNode that contains every Grass TileCategory Enum as its children.
         */
        DefaultMutableTreeNode typeNode = null;
        DefaultMutableTreeNode categoryNode = null;
        
        //Map Tiles
        typeNode = new DefaultMutableTreeNode(SelectionType.TILES);
        top.add(typeNode);
        
        for (TileCategory category : TileCategory.values()) {
            categoryNode = new DefaultMutableTreeNode(category);
            typeNode.add(categoryNode);
        }
        
        //Props
        typeNode = new DefaultMutableTreeNode(SelectionType.PROPS);
        top.add(typeNode);
        
        for (TileCategory category : TileCategory.values()) { //TileCategory needs to be replaced with the appropriate Enum for Props
            categoryNode = new DefaultMutableTreeNode();
            typeNode.add(categoryNode);
        }
        
        //Overlays
        typeNode = new DefaultMutableTreeNode(SelectionType.OVERLAYS);
        top.add(typeNode);
        
        for (OverlayCategory category : OverlayCategory.values()) {
            categoryNode = new DefaultMutableTreeNode(category);
            typeNode.add(categoryNode);
        }
        
        //Entities
        typeNode = new DefaultMutableTreeNode(SelectionType.ENTITIES);
        top.add(typeNode);
        
        for (TileCategory category : TileCategory.values()) { //TileCategory needs to be replaced with the appropriate Enum for Entities
            categoryNode = new DefaultMutableTreeNode();
            typeNode.add(categoryNode);
        }
    }
    
    private void addTreeSelectionListener(JTree tree) {
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent selectionEvent) {
                JTree sourceTree = (JTree)selectionEvent.getSource();
                DefaultMutableTreeNode sourceNode = (DefaultMutableTreeNode)sourceTree.getLastSelectedPathComponent(); //Get the TreeNode which was clicked on from our JTree
                
                if (sourceNode == null) {
                    return;
                }
                
                if (sourceNode.isLeaf() == true) { //Selections are always leaf nodes
                    Object nodeEnum = sourceNode.getUserObject();
                    
                    //This seems to be bad code, but I'm not currently sure how to do it polymorphicly
                    if (nodeEnum instanceof TileCategory) {
                        selection.setSelection(SelectionType.TILES, (TileCategory)nodeEnum);
                    } else if (nodeEnum instanceof OverlayCategory) {
                        selection.setSelection(SelectionType.OVERLAYS, (OverlayCategory)nodeEnum);
                    }
                }
            }
        });
    }
}