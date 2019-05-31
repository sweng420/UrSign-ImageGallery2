package application;
	
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Main extends Application {
	
	int numOfImages = 43;
	
	GridPane gridGalleryPane;
	HBox bigImagePane;
	HBox controlBar;
	BorderPane borderPaneBase;
	Scene scene;
	Stage stageMain;
	Image imageList[] = new Image[numOfImages];
	ImageView imageGalleryNodeList[] = new ImageView[numOfImages];
	ImageView imageLargeNodeList[] = new ImageView[numOfImages];
	String[] imageInfoStrings = new String[numOfImages];
	Label[] imageInfoLabels = new Label[numOfImages];
	File imageFiles;
	int gridStartIndex = 0;
	int gridLastNodeIndex;
	int gridNodeIndex;
	boolean inSlideShow = false;
	int currentSlideNode = 0;
	
	public void start(Stage stage) {
		try {
			
			stageMain = stage;
			
			//Instantiating the Shadow class 
		    DropShadow dropShadow = new DropShadow(); 
		    //setting the type of blur for the shadow 
		    dropShadow.setBlurType(BlurType.GAUSSIAN); 
		    //Setting colour for the shadow 
		    dropShadow.setColor(Color.BLACK); 
		    //Setting the height of the shadow
		    dropShadow.setHeight(5); 
		    //Setting the width of the shadow 
		    dropShadow.setWidth(5); 
		    //Setting the radius of the shadow 
		    dropShadow.setRadius(5); 
		    //setting the offset of the shadow 
		    dropShadow.setOffsetX(3); 
		    dropShadow.setOffsetY(2); 
		    //Setting the spread of the shadow 
		    dropShadow.setSpread(12); 
			
			for (int i = 0; i < numOfImages; i++){
				//Folder "image" is in project folder
				imageFiles = new File("images/"+i+".png");
				imageList[i] = new Image("file:"+imageFiles.getAbsolutePath());
				
				//Create list of ImageView nodes of size 100x100 for image gallery
				imageGalleryNodeList[i] = new ImageView(imageList[i]);
				imageGalleryNodeList[i].setPreserveRatio(true);
				imageGalleryNodeList[i].setFitWidth(150);
				imageGalleryNodeList[i].setFitHeight(150);
				
				//Create list of ImageView nodes with image ratio preserved and 300 width
				imageLargeNodeList[i] = new ImageView(imageList[i]);
				imageLargeNodeList[i].setPreserveRatio(true);
				imageLargeNodeList[i].setFitHeight(300);
				imageLargeNodeList[i].setEffect(dropShadow);
				
				//Create image information for each image
				imageInfoStrings[i] = ("This is image number: "+i);
				imageInfoLabels[i] = new Label(imageInfoStrings[i]);
				imageInfoLabels[i].setStyle("-fx-font-size: 10; -fx-text-fill: Black;");
			}
			
			//Button for showing next gallery selection or next slide
			Button next =  new Button();
			next.setText("Next");
			next.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			//Button for showing previous gallery selection or previous slide
			Button prev =  new Button();
			prev.setText("Previous");
			prev.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			//Button for starting the slide show from the first image
			Button singleImage = new Button();
			singleImage.setText("Single Image");
			singleImage.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			
			//Button to return to the image gallery from the slide show
			Button imgGal = new Button();
			imgGal.setText("Image Gallery");
			imgGal.setStyle("-fx-font-size: 15; -fx-text-fill: Black;");
			imgGal.setDisable(true);
			
			
			//Setup and add buttons to control bar
			controlBar = new HBox();
			controlBar.setSpacing(20);
			controlBar.getChildren().addAll(prev, next, singleImage, imgGal);
			
			//Create grid pane for image gallery image nodes
			gridGalleryPane = new GridPane();
			gridGalleryPane.setAlignment(Pos.CENTER);
			gridGalleryPane.setVgap(15);
			gridGalleryPane.setHgap(15);
			gridGalleryPane.setCenterShape(true);
			gridGalleryPane.setEffect(dropShadow);
			
			//Call fill grid function to add first nine image to grid
			fillGrid(gridStartIndex);
			
			//Filler labels
			Label ph1 = new Label("BorderTop");
			ph1.setStyle("-fx-font-size: 20;");
			Label ph2 = new Label("BorderLeft");
			ph2.setStyle("-fx-font-size: 20;");
			Label ph3 = new Label("BorderBottom");
			ph3.setStyle("-fx-font-size: 20;");
			
			//Initialise pane for slide show
			bigImagePane = new HBox();
			bigImagePane.setAlignment(Pos.CENTER);

			//Initialise base pane for all 
			borderPaneBase = new BorderPane();
			borderPaneBase.setBottom(controlBar);
			borderPaneBase.setCenter(gridGalleryPane);
			
			//Initialise scene and add to stage
			scene = new Scene(borderPaneBase,800,600);
			stage.setScene(scene);
			//stage.setResizable(false);
			stage.show();
			
			next.setOnAction(e->{
				if (gridLastNodeIndex != imageGalleryNodeList.length && inSlideShow == false){
					emptyGrid();
					gridStartIndex = gridStartIndex+3;
					fillGrid(gridStartIndex);
				}else if(currentSlideNode < (imageGalleryNodeList.length-1) && inSlideShow == true){
					currentSlideNode++;
					enterSlideShow(currentSlideNode);
				}
			});
			
			prev.setOnAction(e->{
				if (gridStartIndex != 0 && inSlideShow == false){
					emptyGrid();
					gridStartIndex = gridStartIndex-3;
					fillGrid(gridStartIndex);
				}else if(currentSlideNode != 0 && inSlideShow == true){
					currentSlideNode--;
					enterSlideShow(currentSlideNode);
				}
			});
			
			singleImage.setOnAction(e->{
				currentSlideNode = 0;
				enterSlideShow(currentSlideNode);
				singleImage.setDisable(true);
				imgGal.setDisable(false);
			});
			
			imgGal.setOnAction(e->{
				inSlideShow = false;
				borderPaneBase.setCenter(gridGalleryPane);
				singleImage.setDisable(false);
				imgGal.setDisable(true);
				borderPaneBase.setRight(null);
			});
			
			gridGalleryPane.setOnMouseClicked(e->{
				Node node  = e.getPickResult().getIntersectedNode();
				if (node != gridGalleryPane) {
			        int colIndex = GridPane.getColumnIndex(node);
			        int rowIndex = GridPane.getRowIndex(node);
			        gridNodeIndex = getGirdNum(colIndex, rowIndex);
			        enterSlideShow(gridStartIndex+gridNodeIndex);
			        singleImage.setDisable(true);
					imgGal.setDisable(false);
			    }
			});
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void fillGrid(int start){
		
		for (int j = 0; j < 3; j++){
			for (int k = 0; k < 3; k++){
				gridGalleryPane.add(imageGalleryNodeList[start], k, j);
				start++;
				if (start == imageGalleryNodeList.length){
					break;
				}
			}
		}
		gridLastNodeIndex = start;
	}
	
	
	public void emptyGrid(){
		gridGalleryPane.getChildren().removeAll(imageGalleryNodeList);
	}
	
	public int getGirdNum(int col, int row){
		
		int gridPos = 0;
		
		gridPos = (3*row) + col;
		
		return gridPos;
	}
	
	public void enterSlideShow(int startImageIndex){
		inSlideShow = true;
		if (!(bigImagePane.getChildren().isEmpty())){
			bigImagePane.getChildren().remove(0);
		}
		bigImagePane.getChildren().add(imageLargeNodeList[startImageIndex]);
		
		currentSlideNode = startImageIndex;
		
		borderPaneBase.setCenter(bigImagePane);
		borderPaneBase.setRight(imageInfoLabels[startImageIndex]);
	}
}
