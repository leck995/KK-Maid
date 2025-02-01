package cn.tealc995.kkmaid.ui;

import cn.tealc995.kkmaid.api.model.RoleEx;
import cn.tealc995.kkmaid.util.AnchorPaneUtil;
import cn.tealc995.kkmaid.util.CssLoader;
import cn.tealc995.teaFX.controls.LoadingDot;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-13 07:10
 */
public class CategoryUI {
    private StackPane root;
    private CategoryViewModel viewModel;

    public CategoryUI(CategoryType type) {
        root=new StackPane();
        viewModel=new CategoryViewModel(type);

        GridView<RoleEx> gridView=new GridView<>(viewModel.getItems());
        gridView.setCellWidth(200);
        gridView.setCellFactory(new Callback<GridView<RoleEx>, GridCell<RoleEx>>() {
            @Override
            public GridCell<RoleEx> call(GridView<RoleEx> roleExGridView) {
                GridCell<RoleEx> cell=new GridCell<>(){
                    @Override
                    protected void updateItem(RoleEx roleEx, boolean b) {
                        super.updateItem(roleEx, b);
                        if (roleEx !=null && !b){
                            Button button=new Button(String.format("%s(%d)",roleEx.getName(),roleEx.getCount()));
                            button.setOnAction(event -> {
                                viewModel.search(type,roleEx.getName());
                            });
                            setGraphic(button);
                        }else {
                            setGraphic(null);
                        }
                    }
                };
                cell.setPrefWidth(200);
                return cell;
            }
        });

        Label title=new Label();
        title.getStyleClass().add("category-title");
        title.textProperty().bind(viewModel.titleProperty());

        TextField searchField=new TextField();
        searchField.getStyleClass().add("category-search-field");
        searchField.setPromptText("搜索");
        searchField.textProperty().bindBidirectional(viewModel.searchKeyProperty());
        searchField.setOnAction(actionEvent -> viewModel.setSearchKey(searchField.getText()));
        HBox searchPane=new HBox(searchField);
        searchPane.setAlignment(Pos.CENTER);

        AnchorPane anchorPane=new AnchorPane(title,searchPane,gridView);
        AnchorPaneUtil.setPosition(title,15.0,null,null,15.0);
        AnchorPaneUtil.setPosition(searchPane,40.0,0.0,null,15.0);
        AnchorPaneUtil.setPosition(gridView,100.0,0.0,0.0,15.0);

        LoadingDot loadingDot=new LoadingDot();
        loadingDot.visibleProperty().bind(viewModel.loadingProperty());


        root.getChildren().addAll(anchorPane,loadingDot);
        root.getStyleClass().add("background");
        root.getStylesheets().add(CssLoader.getCss(CssLoader.category));
    }

    public StackPane getRoot() {
        return root;
    }
}