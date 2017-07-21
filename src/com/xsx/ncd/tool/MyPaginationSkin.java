package com.xsx.ncd.tool;

import com.sun.javafx.scene.control.skin.PaginationSkin;

import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class MyPaginationSkin extends PaginationSkin {
	
	private HBox controlBox;
    private Button prev;
    private Button next;
    private Button first;
    private Button last;
    private TextField pageindexinput;
    private Label pagecountlabel;

    private void patchNavigation() {
        Pagination pagination = getSkinnable();

        Node control = pagination.lookup(".control-box");
        if (!(control instanceof HBox))
            return;
        controlBox = (HBox) control;
        
        prev = (Button) controlBox.getChildren().get(0);
        next = (Button) controlBox.getChildren().get(controlBox.getChildren().size() - 1);
        
        first = new Button("Ê×Ò³");
        first.setAlignment(Pos.CENTER);
        first.setOnAction(e -> {
            pagination.setCurrentPageIndex(0);
        });
        first.disableProperty().bind(
                pagination.currentPageIndexProperty().isEqualTo(0));

        last = new Button("Î²Ò³");
        last.setAlignment(Pos.CENTER);
        last.setOnAction(e -> {
            pagination.setCurrentPageIndex(pagination.getPageCount());
        });
        last.disableProperty().bind(
                pagination.currentPageIndexProperty().isEqualTo(
                        pagination.getPageCount() - 1));
        
        pageindexinput = new TextField();
        pageindexinput.prefColumnCountProperty().bind(pageindexinput.textProperty().length());
        pageindexinput.setStyle("-fx-font-size: 15; -fx-text-fill: dodgerblue  ;");
        pageindexinput.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getCode() == KeyCode.ENTER){
					
					if(pageindexinput.getText().length() > 0){

						Integer temp = null;
						
						try {
							temp = Integer.valueOf(pageindexinput.getText());
						} catch (Exception e2) {
							// TODO: handle exception
							temp = null;
						}
						
						if(temp != null){
							if(temp > 0)
								temp -= 1;
							if(temp < pagination.getPageCount())
								pagination.setCurrentPageIndex(temp);

						}
						
						temp = null;
					}
				}
			}
		});
        
        StringBinding indexlabel = new StringBinding() {
        	{
        		bind(pagination.currentPageIndexProperty());
        	}
			@Override
			protected String computeValue() {
				// TODO Auto-generated method stub
				int index = pagination.getCurrentPageIndex();
				
				return index+1+"";
			}
		};

		pageindexinput.textProperty().bind(indexlabel);
        pageindexinput.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if(newValue){
					pageindexinput.textProperty().unbind();
				}	
				else{
					pageindexinput.textProperty().bind(indexlabel);
				}
			}
		});
        
        pagecountlabel = new Label("");
        pagecountlabel.setStyle("-fx-font-size: 15; -fx-text-fill: dodgerblue  ;");
        pagecountlabel.textProperty().bind(new StringBinding() {
        	{
        		bind(pagination.pageCountProperty());
        	}
			@Override
			protected String computeValue() {
				// TODO Auto-generated method stub
				return "/ "+pagination.getPageCount();
			}
		});
        
        ListChangeListener childrenListener = c -> {
            while (c.next()) {
                // implementation detail: when nextButton is added, the setup is complete
                if (c.wasAdded() && !c.wasRemoved() // real addition
                        && c.getAddedSize() == 1 // single addition
                        && c.getAddedSubList().get(0) == next) { 
                    addCustomNodes();
                }
            }
        };
        controlBox.getChildren().addListener(childrenListener);
        
        addCustomNodes();
    }

    protected void addCustomNodes() {
        // guarding against duplicate child exception 
        // (some weird internals that I don't fully understand...)
        if (first.getParent() == controlBox) return;
        controlBox.getChildren().add(0, first);
        controlBox.getChildren().add(last);
        controlBox.getChildren().add(controlBox.getChildren().size()/2+1, pageindexinput);
        controlBox.setMargin(pageindexinput, new Insets(0, 10, 0, 30));
        
        controlBox.getChildren().add(controlBox.getChildren().indexOf(pageindexinput)+1, pagecountlabel);
        controlBox.setMargin(pagecountlabel, new Insets(0, 30, 0, 0));
    }

    /**
     * @param pagination
     */
    public MyPaginationSkin(Pagination pagination) {
        super(pagination);
        patchNavigation();
    }
}
