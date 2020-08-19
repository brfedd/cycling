import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

//--------------------------------------------Initialization of global variables, main method to launch--------------------------------------------------------------------




public class ModelCode extends Application{
	double n=0.409509;                                       
	double thetaFC=0.27393;                                     
	double Sfc=thetaFC/n;                                       
	double CNadd=32.00;                                        
	double Zr=2;                                                
	int NoOfDays = 0; 
	double[] ADD;

	double[] S;
	
	int testDays = 10;
	
	double[] inputNplus;
	double[] inputNminus;
	double[] swc;
	
	NumberAxis xAxis = new NumberAxis();
	
	
	NumberAxis yAxis = new NumberAxis();
	
	
	LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//-------------------------------------------------------------------Initialization of buttons, labels-----------------------------------------------------

	@Override
	public void start(Stage primary) throws Exception {
		
		
		
		
		
		Button run = new Button ("Run");
		run.setLayoutX(20);
		run.setLayoutY(20);
		
		TextField textField = new TextField();
		textField.setLayoutX(20);
		textField.setLayoutY(85);
		
		Label label1 = new Label("Enter the number of days to model. \nMust match the number of days \nin the input file or an error will occur.");
		label1.setLayoutX(20);
		label1.setLayoutY(110);
		
		TextField textField3 = new TextField();
		textField3.setLayoutX(20);
		textField3.setLayoutY(220);
		
		TextField textField4 = new TextField();
		textField4.setLayoutX(20);
		textField4.setLayoutY(320);
				
		Label label3 = new Label("Enter the inital value of \nAmmonium (NPlus)");
		label3.setLayoutX(20);
		label3.setLayoutY(245);
		
		Label label4 = new Label("Enter the initial value of \nNitrate (NMinus)");
		label4.setLayoutX(20);
		label4.setLayoutY(345);
		
		Label label5 = new Label("");
		label5.setLayoutX(60);
		label5.setLayoutY(20);
		label5.setTextFill(Color.web("ff0000"));
		
	//------------------------------------------------------------------------------Drag box code for N Input------------------------------------------------------------	
		
		Label warning1 = new Label("");
		warning1.setLayoutX(300);
		warning1.setLayoutY(40);
		warning1.setTextFill(Color.web("ff0000"));
		
		
		Label label = new Label("Drag and drop N input file here (.txt)");
		VBox dragTarget = new VBox();
		dragTarget.getChildren().add(label);
		dragTarget.setLayoutX(300);
		dragTarget.setLayoutY(20);
		dragTarget.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		dragTarget.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != dragTarget
                        && event.getDragboard().hasFiles()) {
                   
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
			}
		});
		
		 dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {

	            @Override
	            public void handle(DragEvent event) {
	            	int errorcount = 0;
	                Dragboard db = event.getDragboard();
	                boolean success = false;
	                if (db.hasFiles()) {
	                   
	                    success = true;
	                    File file = db.getFiles().get(0);
	                    BufferedReader reader;
	                    try {
	                    int days = Integer.valueOf(textField.getText());
	                    NoOfDays = days;
	                    
	                    inputNplus = new double[NoOfDays+1];
	                    inputNminus = new double[NoOfDays+1];
	                    
		                    try {
		                    	reader = new BufferedReader(new FileReader(file));
		                    	String line = reader.readLine();
		                    	line = reader.readLine(); //Skip the first line (the headers);
		                    	int count = 0;
		                    	while(line!= null) {
		                    		try {
			                    		Scanner s = new Scanner(line);
			                    		s.next();
			                    		double nit = Double.valueOf(s.next());
			                    		double amm = Double.valueOf(s.next());
			                    		try {
			                    			inputNplus[count] = amm;
			                    			inputNminus[count] = nit;
			                    		}catch(Exception e4) {
			                    			warning1.setText("The number of days you entered\nis less than the number of \nentries in the N input file.");
			                    			//System.out.println("The number of days you entered is less than the number of entries in the N input file.");
			                    			label.setText("Error reading N input file. Try again.");
			                    			errorcount++;
			                    			break;
			                    		}
			                    		s.close();
			                    		line = reader.readLine();
			                    		count ++;
		                    		}catch(Exception e2){
		                    			warning1.setText("Please ensure that the file is \na .txt file and check the format \nin the file.");
		                    			//System.out.println("Please ensure that the file is a .txt file and check the format in the file.");
		                    			label.setText("Error reading N input file. Try again.");
		                    			errorcount++;
		                    			break;
		                    		}
		                    	}
		                    	if(count!= NoOfDays) {
		                    		if(errorcount == 0) {
		                    			warning1.setText("The number of days entered is \ngreater than the number of \nentries in the N input file.");
		                    			//System.out.println("The number of days entered is greater than the number of enteries in the N input file.");
		                    			label.setText("Error reading N input file. Try again.");
		                    			errorcount++;
		                    		}
		                    	}
		                    	
		                    }catch(IOException e1) {
		                    	warning1.setText("Please ensure that the file \nis a .txt file.");
		                    	//System.out.println("Please ensure that the file is a .txt file.");
		                    	label.setText("Error opening N input file. Try again.");
		                    	errorcount++;
		                    }
	                    }catch(Exception e3) {
	                    	warning1.setText("Please ensure that you have entered \nan integer value for the number of days.");
	                    	//System.out.println("Please ensure that you have entered an integer value for the number of days.");
	                    	label.setText("Error opening N input file. Try again.");
	                    	errorcount++;
	                    }
	                   
	                }
	                event.setDropCompleted(success);
	                if(errorcount == 0) {
	                	 warning1.setText("");
	                	//System.out.println("");
	                	 label.setText("Successfully uploaded N input file.");
	                	 
	                }
	                event.consume();
	            }
	        });
		
		
		//------------------------------------------------------------------------------Drag box code for SWC------------------------------------------------------------	
			
		    Label warning2 = new Label("");
		    warning2.setLayoutX(550);
		    warning2.setLayoutY(40);
		    warning2.setTextFill(Color.web("ff0000"));
			Label label2 = new Label("Drag and drop swc input file here (.txt)");
			VBox dragTarget2 = new VBox();
			dragTarget2.getChildren().add(label2);
			dragTarget2.setLayoutX(550);
			dragTarget2.setLayoutY(20);
			dragTarget2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			dragTarget2.setOnDragOver(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					if (event.getGestureSource() != dragTarget2
	                        && event.getDragboard().hasFiles()) {
	                   
	                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	                }
	                event.consume();
				}
			});
			
			 dragTarget2.setOnDragDropped(new EventHandler<DragEvent>() {

		            @Override
		            public void handle(DragEvent event) {
		            	int errorcount = 0;
		                Dragboard db = event.getDragboard();
		                boolean success = false;
		                if (db.hasFiles()) {
		                   
		                    success = true;
		                    File file = db.getFiles().get(0);
		                    BufferedReader reader;
		                    try {
		                    int days = Integer.valueOf(textField.getText());
		                    NoOfDays = days;
		                    
		                    swc = new double[NoOfDays];
		                   
			                    try {
			                    	reader = new BufferedReader(new FileReader(file));
			                    	String line = reader.readLine();
			                    	line = reader.readLine(); //Skip the first line (the headers)
			                    	int count = 0;
			                    	while(line!= null) {
			                    		try {
				                    		Scanner s = new Scanner(line);
				                    		s.next(); //Skip the day entry in the file
				                    		double currentswc = Double.valueOf(s.next());
				                    		
				                    		try {
				                    			swc[count] = currentswc;
				                    		}catch(Exception e4) {
				                    			warning2.setText("The number of days you entered\n is less than the number of\n entries in the swc input file.");
				                    			//System.out.println("The number of days you entered is less than the number of entries in the swc input file.");
				                    			label2.setText("Error reading swc input file. Try again.");
				                    			errorcount++;
				                    			break;
				                    		}
				                    		s.close();
				                    		line = reader.readLine();
				                    		count ++;
			                    		}catch(Exception e2){
			                    			warning2.setText("Please ensure that the file is\na .txt file and check the format\nin the file.");
			                    			//System.out.println("Please ensure that the file is a .txt file and check the format in the file.");
			                    			label2.setText("Error reading swc input file. Try again.");
			                    			errorcount++;
			                    			break;
			                    		}
			                    	}
			                    	if(count!= NoOfDays) {
			                    		if(errorcount == 0) {
			                    			warning2.setText("The number of days entered is \ngreater than the number of\nentries in the swc input file.");
			                    			//System.out.println("The number of days entered is greater than the number of enteries in the swc input file.");
			                    			label2.setText("Error reading swc input file. Try again.");
			                    			errorcount++;
			                    		}
			                    	}
			                    	
			                    }catch(IOException e1) {
			                    	warning2.setText("Please ensure that the file \nis a .txt file.");
			                    	//System.out.println("Please ensure that the file is a .txt file.");
			                    	label2.setText("Error opening swc input file. Try again.");
			                    	errorcount++;
			                    }
		                    }catch(Exception e3) {
		                    	warning2.setText("Please ensure that you have entered\nan integer value for the number of days.");
		                    	//System.out.println("Please ensure that you have entered an integer value for the number of days.");
		                    	label2.setText("Error opening swc input file. Try again.");
		                    	errorcount++;
		                    }
		                   
		                }
		                event.setDropCompleted(success);
		                if(errorcount == 0) {
		                	 warning2.setText("");
		                	 //System.out.println("");
		                	 label2.setText("Successfully uploaded swc input file.");
		                	 
		                }
		                event.consume();
		            }
		        });
			
			
		
		
		
		//----------------------------------------------------------------------Code for when the run button is pushed (main equation)-----------------------------------------------
		
		
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            try {
              S = swc.clone();
        	  for(int i = 0; i < S.length; i++) {
        		  S[i] = S[i]/n;
        	  }
        	  /*
        	  double Nplus0 = inputNplus[0];
        	  double Nminus0 = inputNminus[0];
              double CNl = 13.8;
              double CNh = 10.74;
              double CNb = 8.0;
              double Cl0 = 33.67;
              double Ch0 = 4457.99;
              double Cb0 = 57.88;
              
              double Nl0 = Cl0/CNl;
              double Nh0 = Ch0/CNh;
              double Nb0 = Cb0/CNb;
              
              double aminus = 1;
              double aplus = 0.05;
              double b = 5.5;
              double Ks = 0.5;
              double rh = 0.25;
              double rr = 0.6;
              double kd = 0.0085;
              double kl = 0.000065;
              double kh = 0.0000025;
              double kn = 0.6;
              double kiplus = 1;
              double kiminus = 1;
              */
        	  
        	  double Nplus0 = Double.valueOf(textField3.getText());
        	  double Nminus0 = Double.valueOf(textField4.getText());
              double CNl = 13.74;
              double CNh = 10.75;
              double CNb = 8.0;
              double Cl0 = 32.78;
              double Ch0 = 4250.7;
              double Cb0 = 86.18;
              
              double Nl0 = Cl0/CNl;
              double Nh0 = Ch0/CNh;
              double Nb0 = Cb0/CNb;
              
              double aminus = 0.8;
              double aplus = 0.001;
              double b = 4;
              double Ks = 0.2;
              double rh = 0.23333333333333333334;
              double rr = 0.7666666666666666666;
              double kd = 0.0095;
              double kl = 0.0000549;
              double kh = 0.00000183;
              double kn = 0.001;
              double kiplus = 0.5;
              double kiminus = 0.5;
              ADD = new double[NoOfDays];
              //Initialize array sizes to NoOfDays+1 to a void an index out of bound exception in the for loop
              double[] Cl = new double[NoOfDays+1];
              double[] Ch = new double[NoOfDays+1];
              double[] Cb = new double[NoOfDays+1];
              double[] Nl = new double[NoOfDays+1];
              double[] Nh = new double[NoOfDays+1];
              double[] Nb = new double[NoOfDays+1];
              double[] Nplus = new double[NoOfDays+1];
              double[] Nminus = new double[NoOfDays+1];
              
              Cl[0] = Cl0;
              Ch[0] = Ch0;
              Cb[0] = Cb0;
              Nl[0] = Nl0;
              Nh[0] = Nh0;
              Nb[0] = Nb0;
              Nplus[0] = Nplus0;
              Nminus[0] = Nminus0;
              
              if( rh >= (CNh/CNl)) {
            	  rh = (CNh/CNl);
              }
              if (rr >= (1-rh)) {
            	  rr = 1-rh;
              }
              for(int d = 0; d < NoOfDays; d++) {
            	  double condition=(kh*Ch[d]*(1/CNh - (1-rr)/CNb) + kl*Cl[d]*(1/CNl - rh/CNh - (1-rh-rr)/CNb));
            	  double phi = 1;
            	  //IMM and MIN variables must be declared outside of the conditionals in Java, but they will
            	  //still be initialized inside the conditionals just like the python code
            	  double IMM = 0;
            	  double MIN = 0;
            	  if(condition > 0) {
            		  phi = 1;
            		  MIN = phi*fd(S[d])*Cb[d]*(kh*Ch[d]*(1/CNh - (1-rr)/CNb) + kl*Cl[d]*(1/CNl - rh/CNh - (1-rh-rr)/CNb));
            		  IMM = 0;
            	  }
            	  else {
            		  double IMMmax=(kiplus*Nplus[d]+kiminus*Nminus[d])*fd(S[d])*Cb[d];
            		  if(-phi*fd(S[d])*Cb[d]*(kh*Ch[d]*(1/CNh - (1-rr)/CNb) + kl*Cl[d]*(1/CNl - rh/CNh - (1-rh-rr)/CNb))<=IMMmax) {
            			  phi = 1;
            		  }
            		  else {
            			  phi=-(kiplus*Nplus[d]+kiminus*Nminus[d])/(kh*Ch[d]*(1/CNh - (1-rr)/CNb) + kl*Cl[d]*(1/CNl - rh/CNh - (1-rh-rr)/CNb));
            		  }
            		  MIN = 0;
            		  IMM=-phi*fd(S[d])*Cb[d]*(kh*Ch[d]*(1/CNh - (1-rr)/CNb) + kl*Cl[d]*(1/CNl - rh/CNh - (1-rh-rr)/CNb));
            	  }
            	  double BD = kd*Cb[d];
            	  double  DECl = phi*fd(S[d])*kl*Cb[d]*Cl[d];
            	  double DECh = phi*fd(S[d])*kh*Cb[d]*Ch[d];
            	  
            	  Cl[d+1] = Cl[d] + ADD[d] + BD - DECl;
            	  Nl[d+1] = Nl[d] + ADD[d]/CNadd +  BD/CNb -DECl/CNl ;
            	  Ch[d+1] = Ch[d] + rh*DECl - DECh;
            	  
            	  Nh[d+1] = Ch[d+1]/CNh;
            	  Cb[d+1] = Cb[d] + (1-rh-rr)*DECl + (1-rr)*DECh - BD;
            	  
            	  Nb[d+1] = Cb[d+1]/CNb;
            	  
            	  double NIT = fn(S[d])*kn*Cb[d]*Nplus[d];
            	  Nplus[d+1] = Nplus[d] + MIN - kiplus*Nplus[d]*IMM/(kiplus*Nplus[d]+kiminus*Nminus[d]) - NIT - aplus*L(S[d],Ks,b)*Nplus[d]/(S[d]*n*Zr) + inputNplus[d+1];
            	  Nminus[d+1] = Nminus[d] + NIT - kiminus*Nminus[d]*IMM/(kiplus*Nplus[d]+kiminus*Nminus[d]) - aminus*L(S[d],Ks,b)*Nminus[d]/(S[d]*n*Zr) + inputNminus[d+1];
              }
             
          	xAxis.setLabel("Day");
          	
          
          	yAxis.setLabel("Ammonium and Nitrate Concentration");
          
    		
    		XYChart.Series<Number, Number> data1 = new XYChart.Series<>();
    		XYChart.Series<Number, Number> data2 = new XYChart.Series<>();

    		
    				for(int i = 0; i < NoOfDays; i++) {
    					data1.getData().add(new XYChart.Data<Number, Number>(i, Nplus[i]));
    				}
    				for(int j = 0; j < NoOfDays; j++) {
    					data2.getData().add(new XYChart.Data<Number, Number>(j, Nminus[j]));
    				}
    				data1.setName("Ammonium");
    				data2.setName("Nitrate");
    				linechart.getData().add(data1);
    				linechart.getData().add(data2);
    		label5.setText("");
    		
            }catch(Exception ex) {
            	label5.setText("An error occurred. Please ensure that you\nhave entered all input parameters and files.");
            }
            } 
        }; 
		run.setOnAction(event);
		
		
	
	//-------------------------------------------------------------------------Adding all components to the scene (assembling the tool)----------------------------------------------------------	
		
		
		
		Pane pane = new Pane();
		
		pane.getChildren().add(warning2);
		pane.getChildren().add(warning1);
		pane.getChildren().add(textField);
		pane.getChildren().add(label1);
		pane.getChildren().add(dragTarget);
		pane.getChildren().add(dragTarget2);
		pane.getChildren().add(run);
		pane.getChildren().add(textField3);
		pane.getChildren().add(textField4);
		pane.getChildren().add(label3);
		pane.getChildren().add(label4);
		pane.getChildren().add(label5);
		
		linechart.setLayoutX(200);
		linechart.setLayoutY(150);
		pane.getChildren().add(linechart);
		BackgroundFill backgroundfill = new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(backgroundfill);
		pane.setBackground(background);
		
		Scene scene = new Scene(pane, 780, 600);
		
		primary.setScene(scene);
		primary.setTitle("Modeling Tool");
		primary.show();
	}
	
	//-------------------------------------------------------------------Helper methods-------------------------------------------------------------------------------------
	
	public double fd(double s) {
		if(s <= Sfc) {
			return s/Sfc;
		}else {
			
			return Sfc/s;
		}
	}
	public double fn(double s) {
		if (s <= Sfc) {
			return s/Sfc;
		}else {
			return (1-s)/(1-Sfc);
		}
	}
	public double L(double s, double Ks, double b) {
		if(s > Sfc) {
			double beta = 2 * b + 4;
			return Ks * (Math.exp(beta*(s-Sfc))-1)/(Math.exp(beta*(1-Sfc))-1);
		}else {
			return 0;
		}
	}
}
