import java.security.MessageDigest;
import java.sql.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TestTaker_Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	TeacherAccount userSession;

	private Stage stage;

	final int stageHeight = 300;
	final int stageWidth = 650;

	@Override
	public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
		this.stage = primaryStage;

		// stage.setMinHeight(300);
		// stage.setMinWidth(600);

		stage.setMaxHeight(300);
		stage.setMaxWidth(650);

		// start page

		// login page

		// go to login scene

		// go to take test scene
		// to do

		// title
		primaryStage.setTitle("Test Taker");

		// start scene on start
		primaryStage.setScene(getStartScene());
		primaryStage.show();

		// System.out.println(takeTestBtn.getWidth());

	}

	private Scene getStartScene() {
		BorderPane startPage = new BorderPane();
		VBox startButtons = new VBox();

		startPage.setMinWidth(650);

		Text titleText = new Text("TestTaker");
		titleText.setStyle("-fx-font-size: 5em;");

		Button signInBtn = new Button("Sign In");
		Button takeTestBtn = new Button("Take Test");

		signInBtn.setStyle("-fx-font-size: 2em; -fx-border-color: black;");
		takeTestBtn.setStyle("-fx-font-size: 2em; -fx-border-color: black;");
		signInBtn.setPrefWidth(118);
		startButtons.setSpacing(10);

		startButtons.getChildren().addAll(signInBtn, takeTestBtn);
		startButtons.setAlignment(Pos.CENTER);
		startButtons.setPadding(new Insets(50, 250, 100, 250));
		// startPage.setStyle("-fx-background-color: #39a8db;");

		BorderPane.setAlignment(titleText, Pos.TOP_CENTER);

		startPage.setCenter(startButtons);
		startPage.setTop(titleText);

		Scene sStartPage = new Scene(startPage);

		signInBtn.setOnAction(e -> {
			try {
				stage.setScene(getLoginPage());
			} catch (ClassNotFoundException | SQLException e1) {

				e1.printStackTrace();
			}
		});

		takeTestBtn.setOnAction(e -> stage.setScene(testIdPage()));

		return sStartPage;

	}

	private Scene getLoginPage() throws SQLException, ClassNotFoundException {
		Text loginCaption = new Text("\n");
		loginCaption.setFill(Color.RED);

		GridPane loginGp = new GridPane();
		// loginGp.setPadding(new Insets(25, 200, 25, 200));
		Text unameText = new Text("Username:");
		Text passText = new Text("Password:");
		TextField unameTf = new TextField();
		PasswordField passTf = new PasswordField();
		Button btnSubmit = new Button("Submit");
		BorderPane masterLoginPane = new BorderPane();
		masterLoginPane.setCenter(loginGp);
		// Text loginText = new Text("\nLOGIN");
		// loginText.setStyle("-fx-font-size: 4em;");

		// BorderPane.setAlignment(loginText, Pos.TOP_CENTER);

		// masterLoginPane.setTop(loginText);

		BackButton backFromLoginScreen = new BackButton();

		masterLoginPane.setLeft(backFromLoginScreen);

		backFromLoginScreen.setOnAction(e -> stage.setScene(getStartScene()));

		loginGp.add(unameText, 0, 0);
		loginGp.add(unameTf, 1, 0);
		loginGp.add(passText, 0, 1);
		loginGp.add(passTf, 1, 1);
		loginGp.add(loginCaption, 0, 2, 2, 1);
		// loginGp.add(btnSubmit, 0, 2);

		// BorderPane.setAlignment(btnSubmit, Pos.BOTTOM_CENTER);

		btnSubmit.setPadding(new Insets(15, 22, 15, 22));

		btnSubmit.setAlignment(Pos.CENTER);
		loginGp.setAlignment(Pos.CENTER);

		// BorderPane.setMargin(btnSubmit, new Insets(12, 12, 12, 12));

		VBox gpWithSubmit = new VBox();
		gpWithSubmit.setPadding(new Insets(100, 250, 10, 150));

		gpWithSubmit.getChildren().addAll(loginGp, btnSubmit);

		masterLoginPane.setCenter(gpWithSubmit);

		Hyperlink toCreateAccount = new Hyperlink("Don't have an account?");

		toCreateAccount.setOnAction(e -> stage.setScene(getAccountCreationScene()));

		masterLoginPane.setBottom(toCreateAccount);

		gpWithSubmit.setAlignment(Pos.CENTER);

		// masterLoginPane.setStyle("-fx-background-color: #39a8db;");

		Scene sLogin = new Scene(masterLoginPane);

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/finalproj", "admin", "password");

		btnSubmit.setOnAction(e -> {

			try {
				userSession = doLogin(connection, unameTf.getText(), passTf.getText());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			if (userSession == null) {
				loginCaption.setText("Incorrect username or password\n");
			} else {
				System.out.println("Login success\n" + userSession.getTeacherName());
				try {
					stage.setScene(createTeacherHome());
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}

		});
		return sLogin;
	}

	private Scene getAccountCreationScene() {
		StackPane root = new StackPane();
		GridPane form = new GridPane();
		form.add(new Text("username: "), 0, 0);
		form.add(new Text("name: "), 0, 1);
		form.add(new Text("password: "), 0, 2);
		form.add(new Text("confirm password: "), 0, 3);

		TextField unameTf = new TextField();
		TextField nameTf = new TextField();
		PasswordField passTf = new PasswordField();
		PasswordField confirmPassTf = new PasswordField();

		form.add(unameTf, 1, 0);
		form.add(nameTf, 1, 1);
		form.add(passTf, 1, 2);
		form.add(confirmPassTf, 1, 3);

		VBox formWithSubmit = new VBox();

		Button btSubmit = new Button("Submit");
		btSubmit.setPadding(new Insets(15, 22, 15, 22));
		// root.setPadding(new Insets(100, 200, 100, 200));

		Text caption = new Text();
		caption.setFill(Color.RED);

		formWithSubmit.getChildren().addAll(form, caption, btSubmit);
		root.getChildren().add(formWithSubmit);
		form.setAlignment(Pos.CENTER);
		formWithSubmit.setAlignment(Pos.CENTER);

		BackButton backToLogin = new BackButton();
		StackPane.setAlignment(backToLogin, Pos.TOP_LEFT);

		root.getChildren().add(backToLogin);

		root.setMinWidth(650);
		root.setMinHeight(300);

		backToLogin.setOnAction(e -> {
			try {
				stage.setScene(getLoginPage());
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		});

		btSubmit.setOnAction(e -> {
			caption.setText("");
			String username = unameTf.getText();
			String name = nameTf.getText();
			String password = passTf.getText();
			String confirmPassword = confirmPassTf.getText();

			Boolean canCreate = true;

			if ((username.length() < 0) && (name.length() < 0) && (password.length() < 0)) {
				caption.setText("please fill all fields\n");
				canCreate = false;
			}
			if (password.length() < 7) {
				caption.setText(caption.getText() + "password must be at least 8 characters long\n");
				canCreate = false;
			}
			if (password.equals(confirmPassword) == false) {
				caption.setText(caption.getText() + "passwords do not match\n");
				canCreate = false;
			}
			if (isAlphanumeric(username) == false) {
				caption.setText(caption.getText() + "username must be alphanumeric\n");
				canCreate = false;
			}
			if (isLettersAndSpaces(name) == false) {
				caption.setText(caption.getText() + "your name may only include characters and spaces\n");
				canCreate = false;
			}

			if (canCreate == true) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e2) {
					e2.printStackTrace();
				}

				Connection connection = null;
				try {
					connection = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "admin", "password");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				try {
					ResultSet resultSet = statement
							.executeQuery("SELECT * FROM finalproj.teachers WHERE username = '" + username + "';");

					if (resultSet.next() == false) {
						String hashedPw = hash256(password);
						statement.executeUpdate("insert into finalproj.teachers values(default, '" + username + "', '"
								+ name + "', '" + hashedPw + "')");
						stage.setScene(getLoginPage());
					} else {
						canCreate = false;
						caption.setText(caption.getText() + "username is taken\n");
					}
				} catch (SQLException e1) {

				} catch (ClassNotFoundException e1) {

					e1.printStackTrace();
				}
			}

		});

		return new Scene(root);
	}

	private TeacherAccount doLogin(Connection c, String un, String pass) throws SQLException {
		Statement sql = c.createStatement();
		String pw256 = hash256(pass);
		ResultSet resultSet = sql.executeQuery(
				"SELECT * FROM finalproj.teachers WHERE username = '" + un + "' AND teacherpw  = '" + pw256 + "';");
		;

		if (resultSet.next() == true) {
			return new TeacherAccount(resultSet.getLong(1), resultSet.getString(3), resultSet.getString(2));
		} else {
			return null;
		}
	}

	private Scene createTeacherHome() throws Exception {
		BorderPane teacherBP = new BorderPane();

		Class.forName("com.mysql.cj.jdbc.Driver");
		// System.out.println("Driver loaded");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "admin", "password");
		// System.out.println("database connected");

		Statement sql = connection.createStatement();

		teacherBP.setPrefSize(stageWidth, stageHeight);

		VBox mainTestBox = new VBox();
		mainTestBox.setSpacing(15);
		ResultSet resultSet = sql
				.executeQuery("select * from finalproj.quizzes where teacherid = '" + userSession.getAcctNum() + "' ORDER BY QUIZID DESC;");
		while (resultSet.next()) {
			mainTestBox.getChildren().add(new QuizBox(resultSet.getString(3), resultSet.getInt(1)));
		}

		if (mainTestBox.getChildrenUnmodifiable().isEmpty()) {
			mainTestBox.getChildren().add(new Text("You have no tests"));
		}

		Button btNew = new Button("create new");
		//btNew.setPadding(new Insets(15, 22, 15, 22));
		//teacherBP.setBottom(btNew);
		//BorderPane.setMargin(btNew, new Insets(15, 0, 15, 0));

		ScrollPane sp = new ScrollPane();
		sp.setContent(mainTestBox);
		//sp.setFitToHeight(true);
		teacherBP.setCenter(sp);
		sp.setPadding(new Insets(10, 10, 10, 10));
		// sp.setMinHeight(stageHeight - 125);
		//sp.setMaxHeight(stageHeight - 150);

		Button btLogout = new Button("Logout");

		HBox topBox = new HBox();
		//topBox.setMinWidth(650);
		topBox.setSpacing(330);

		topBox.getChildren().addAll(new Text("Welcome, " + userSession.getTeacherName() + "!"), new HBox(btNew, btLogout));

		btLogout.setOnAction(e -> logout());

		btNew.setOnAction(e -> stage.setScene(createTest()));

		teacherBP.setTop(topBox);

		//BorderPane.setAlignment(btLogout, Pos.TOP_RIGHT);
		//BorderPane.setAlignment(btNew, Pos.BOTTOM_CENTER);
		
		//BorderPane.setMargin(topBox, new Insets(0));
		BorderPane.setMargin(sp, new Insets(20));
		
		//topBox.setMaxHeight(10);
		
		
		
		
		
		Scene sceneOut = new Scene(teacherBP);
		
		//System.out.println(topBox.getHeight());
		//btNew.set
		
		//teacherBP.setPrefSize(0, 0);
		//teacherBP.setPrefSize(stageWidth, stageHeight);

		return sceneOut;
	}

	class QuizBox extends GridPane {
		private int quizID;

		QuizBox(String quizName, int quizID) {
			this.quizID = quizID;
			super.add(new Text(quizName + " - ID Number: " + quizID), 0, 0);
			Button btViewSubmissions = new Button("View Sumbissions");
			Button btViewQuestions = new Button("View Questions");
			Button btDelete = new Button("Delete");
			VBox btnBox = new VBox();
			btnBox.getChildren().addAll(btViewQuestions, btViewSubmissions, btDelete);
			super.add(btnBox, 0, 1);

			btViewSubmissions.setOnAction(e -> {
				try {
					loadSubmissions();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			});
			btViewQuestions.setOnAction(e -> {
				try {
					loadQuestions();
				} catch (Exception e2) {

					e2.printStackTrace();
				}
			});
			btDelete.setOnAction(e -> {
				try {
					deleteQuiz();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		private void deleteQuiz() throws Exception {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/finalproj", "admin",
					"password");

			Statement statement = connection.createStatement();

			statement.executeUpdate("DELETE FROM finalproj.submissions WHERE quizid = '" + quizID + "';");
			statement.executeUpdate("DELETE FROM finalproj.questions WHERE quizid = '" + quizID + "';");
			statement.executeUpdate("DELETE FROM finalproj.quizzes WHERE quizid = '" + quizID + "';");

			stage.setScene(createTeacherHome());

		}

		private void loadQuestions() throws Exception {

			VBox questions = new VBox();

			questions.setSpacing(10);

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/finalproj", "admin",
					"password");
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM questions WHERE quizid = '" + this.quizID + "';");

			BackButton backToTP = new BackButton();

			backToTP.setOnAction(e -> {
				try {
					stage.setScene(createTeacherHome());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});

			questions.getChildren().add(backToTP);

			int i = 1;
			while (resultSet.next()) {
				char correct;
				switch (resultSet.getInt(9)) {
				case 1:
					correct = 'a';
					break;
				case 2:
					correct = 'b';
					break;
				case 3:
					correct = 'c';
					break;
				case 4:
					correct = 'd';
					break;
				default:
					correct = 'a';
					break;
				}
				questions.getChildren()
						.add(new Text(i + ". " + resultSet.getString(3) + "\n     a. " + resultSet.getString(5)
								+ "\n     b. " + resultSet.getString(6) + "\n     c. " + resultSet.getString(7)
								+ "\n     d. " + resultSet.getString(8) + "\n\n     correct: " + correct));
				++i;
			}

			ScrollPane qPane = new ScrollPane(questions);
			qPane.setFitToHeight(true);

			qPane.setMinHeight(stageHeight - 20);
			qPane.setMinWidth(stageWidth - 20);

			qPane.setPadding(new Insets(10, 10, 10, 10));

			stage.setScene(new Scene(qPane));

		}

		private void loadSubmissions() throws Exception {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/finalproj", "admin",
					"password");
			Statement statement = connection.createStatement();

			VBox submissionBox = new VBox();
			submissionBox.setSpacing(15);
			//submissionBox.setPadding(new Insets(15));

			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM finalproj.submissions WHERE quizid = '" + this.quizID + "';");

			while (resultSet.next()) {
				submissionBox.getChildren().add(new Text(resultSet.getString(3) + ":\n\tScore/100: "
						+ resultSet.getDouble(4) + "\n\tQuestions correct: " + resultSet.getInt(5)));
			}

			if (submissionBox.getChildren().isEmpty()) {
				submissionBox.getChildren().add(new Text("this test has no submissions"));
			}

			BackButton backToTP = new BackButton();

			ScrollPane sp = new ScrollPane(submissionBox);
			// sp.setPadding(new Insets(0,0,20,0));
			StackPane root = new StackPane();

			//BorderPane.setMargin(sp, new Insets(0, 0, 10, 0));
			//BorderPane.setMargin(backToTP, new Insets(0, 0, 0, 0));
			//BorderPane.setAlignment(sp, Pos.CENTER);
			root.getChildren().addAll(sp, backToTP);
			
			StackPane.setMargin(sp, new Insets(25));
			//StackPane.setAlignment(sp, Pos.CENTER);
			StackPane.setAlignment(backToTP, Pos.TOP_LEFT);
			
			sp.setMaxHeight(stageHeight - 150);
			// sp.setMinHeight(stageHeight - 100);
			sp.setMaxWidth(stageWidth - 60);
			//sp.setFitToHeight(true);
			root.setPrefSize(stageWidth, stageHeight);
			//root.setBottom(new Text("\n\n\n\n"));
			
			sp.setTranslateY(-20);
			
			stage.setScene(new Scene(root));

			backToTP.setOnAction(e -> {
				try {
					stage.setScene(createTeacherHome());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});

		}


		public int getQuizID() {
			return quizID;
		}
	}

	private Scene createTest() {
		StackPane root = new StackPane();
		// root.setMinHeight(stageHeight);
		// root.setMinWidth(stageWidth);

		BackButton backToTeacherHome = new BackButton();

		TextField qNumTf = new TextField();
		TextField qNameTf = new TextField();
		qNumTf.setMaxWidth(150);

		qNumTf.setPromptText("number of questions");
		qNameTf.setPromptText("quiz name");
		VBox tfAndNext = new VBox();
		Button next = new Button("next ->");

		StackPane.setAlignment(tfAndNext, Pos.CENTER);
		StackPane.setAlignment(backToTeacherHome, Pos.TOP_LEFT);

		Text caption = new Text();

		tfAndNext.getChildren().addAll(new Text("quiz name:"), qNameTf, new Text("number of questions: "), qNumTf, next,
				caption);
		tfAndNext.setPadding(new Insets(100, 250, 100, 250));

		root.getChildren().addAll(tfAndNext, backToTeacherHome);

		backToTeacherHome.setOnAction(e -> {
			try {
				stage.setScene(createTeacherHome());
			} catch (Exception e2) {

			}
		});

		next.setOnAction(e -> {
			String numString = qNumTf.getText();
			if (isInteger(numString)) {
				int qNum = Integer.parseInt(numString);
				try {
					stage.setScene(getTestCreationPage(qNameTf.getText(), qNum));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				caption.setFill(Color.RED);
				caption.setText("please enter an integer other than zero");
			}
		});

		return new Scene(root);
	}

	private Scene getTestCreationPage(String quizName, int qNum) throws Exception {
		BorderPane root = new BorderPane();

		Button cancelBtn = new Button("cancel");
		Button submitBtn = new Button("submit");

		root.setMinHeight(stageHeight);
		root.setMinWidth(stageWidth);

		TestMaker[] questionFields = new TestMaker[qNum];

		VBox questionVB = new VBox();

		for (int i = 0; i < questionFields.length; ++i) {
			questionFields[i] = new TestMaker(i + 1);
			questionVB.getChildren().add(questionFields[i]);
		}

		questionVB.getChildren().add(submitBtn);

		questionVB.setSpacing(15);

		ScrollPane sp = new ScrollPane(questionVB);
		sp.setFitToHeight(true);
		sp.setMaxHeight(stageHeight - 100);
		sp.setMaxWidth(stageWidth - 60);
		sp.setPadding(new Insets(10, 10, 10, 10));

		root.setPadding(new Insets(10, 10, 10, 10));

		root.setCenter(sp);

		root.setTop(cancelBtn);
		BorderPane.setMargin(cancelBtn, new Insets(0, 0, 10, 0));

		// BorderPane.setAlignment(cancelBtn, Pos.TOP_RIGHT);
		cancelBtn.setTranslateX(20);

		BorderPane.setAlignment(sp, Pos.TOP_CENTER);

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "admin", "password");
		Statement statement = connection.createStatement();
		cancelBtn.setOnAction(e -> {
			try {
				stage.setScene(createTeacherHome());
			} catch (Exception e1) {

			}
		});

		submitBtn.setOnAction(e -> {
			try {
				statement.executeUpdate("INSERT INTO finalproj.quizzes VALUES (DEFAULT, '" + userSession.getAcctNum()
						+ "', '" + quizName + "', '" + qNum + "') ");
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int quizId = -1;
			try {
				ResultSet resultSet = statement.executeQuery("SELECT * FROM finalproj.quizzes WHERE teacherid = '"
						+ userSession.getAcctNum() + "' AND quizname = '" + quizName + "';");
				if (resultSet.next()) {
					quizId = resultSet.getInt(1);
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			for (int i = 0; i < questionFields.length; ++i) {
				try {
					statement.executeUpdate("INSERT INTO finalproj.questions VALUES (DEFAULT, '" + quizId + "', '"
							+ questionFields[i].getQText() + "', '" + i + "', '" + questionFields[i].getO1Text()
							+ "', '" + questionFields[i].getO2Text() + "', '" + questionFields[i].getO3Text() + "', '"
							+ questionFields[i].getO4Text() + "', '" + questionFields[i].getCorrect() + "')");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			try {
				stage.setScene(createTeacherHome());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		return new Scene(root);
	}

	class TestMaker extends VBox {
		HBox question = new HBox();
		HBox option1 = new HBox();
		HBox option2 = new HBox();
		HBox option3 = new HBox();
		HBox option4 = new HBox();

		TextField qTf = new TextField();
		TextField o1Tf = new TextField();
		TextField o2Tf = new TextField();
		TextField o3Tf = new TextField();
		TextField o4Tf = new TextField();

		ChoiceBox<String> cbCorrect = new ChoiceBox<String>();

		TestMaker(int num) {

			qTf.setMaxWidth(150);
			o1Tf.setMaxWidth(100);
			o2Tf.setMaxWidth(100);
			o3Tf.setMaxWidth(100);
			o4Tf.setMaxWidth(100);

			question.getChildren().addAll(new Text(Integer.toString(num) + ". "), qTf);
			option1.getChildren().addAll(new Text("\t      a. "), o1Tf);
			option2.getChildren().addAll(new Text("\t      b. "), o2Tf);
			option3.getChildren().addAll(new Text("\t      c. "), o3Tf);
			option4.getChildren().addAll(new Text("\t      d. "), o4Tf);

			cbCorrect.getItems().add("a. ");
			cbCorrect.getItems().add("b. ");
			cbCorrect.getItems().add("c. ");
			cbCorrect.getItems().add("d. ");

			super.getChildren().addAll(question, option1, option2, option3, option4, cbCorrect);
		}

		public int getCorrect() {
			String value = cbCorrect.getValue();
			
			if(value == null) return 1;

			switch (value) {
			case ("a. "):
				return 1;
			case ("b. "):
				return 2;
			case ("c. "):
				return 3;
			case ("d. "):
				return 4;
			default:
				return 1;
			}
		}

		public String getQText() {
			return qTf.getText();
		}

		public String getO1Text() {
			return o1Tf.getText();
		}

		public String getO2Text() {
			return o2Tf.getText();
		}

		public String getO3Text() {
			return o3Tf.getText();
		}

		public String getO4Text() {
			return o4Tf.getText();
		}
	}

	private void logout() {
		userSession = null;
		stage.setScene(getStartScene());
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		if (Integer.parseInt(s) == 0) {
			return false;
		}
		return true;
	}

	private Scene testIdPage() {
		// StackPane root = new StackPane();
		TextField testIdTf = new TextField();
		TextField nameTf = new TextField();

		testIdTf.setPromptText("test id");
		nameTf.setPromptText("your name");

		testIdTf.setPrefSize(50, 30);
		nameTf.setPrefSize(50, 30);

		Text caption = new Text("");
		caption.setFill(Color.RED);

		Text title = new Text("enter test id: ");
		Text title2 = new Text("enter your name: ");

		title.setStyle("-fx-font-size: 2em;");
		title2.setStyle("-fx-font-size: 2em;");

		VBox titleBox = new VBox(title, testIdTf, title2, nameTf, caption);

		// root.setMinWidth(stageWidth);
		// root.setMinHeight(stageHeight);

		BorderPane pane = new BorderPane();

		pane.setPrefWidth(stageWidth);
		pane.setPrefHeight(stageHeight);

		pane.setPadding(new Insets(50));

		BackButton btnBack = new BackButton();
		Button btnContinue = new Button("next");
		pane.setCenter(titleBox);
		pane.setTop(btnBack);
		pane.setBottom(btnContinue);

		BorderPane.setAlignment(btnBack, Pos.TOP_LEFT);
		BorderPane.setAlignment(btnContinue, Pos.BOTTOM_RIGHT);
		// StackPane.setAlignment(testIdTf, Pos.CENTER);
		// StackPane.setAlignment(titleBox, Pos.BOTTOM_CENTER);

		btnBack.setOnAction(e -> stage.setScene(getStartScene()));

		btnContinue.setOnAction(e -> {
			Connection connection;
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "admin", "password");
				Statement statement;
				statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT * FROM finalproj.quizzes WHERE quizid = '" + testIdTf.getText() + "';");
				if (resultSet.next()) {
					if (nameTf.getText().isEmpty() == false) {
						System.out.println("to quiz");
						// System.out.println(nameTf.getText());
						stage.setScene(getQuizQuestions(statement, resultSet.getInt(1), resultSet.getInt(4),
								nameTf.getText()));
					} else {
						caption.setText("please enter a name");
					}
				} else {
					caption.setText("no quiz exists with id " + testIdTf.getText());
				}
			} catch (SQLException e1) {

			}
			;

		});

		return new Scene(pane);
	}

	private Scene getQuizQuestions(Statement sql, int quizid, int numOfQ, String name) throws SQLException {
		// QuestionBox[] questions = new QuestionBox[numOfQ];

		ResultSet rs = sql
				.executeQuery("SELECT * FROM finalproj.questions WHERE quizid = '" + quizid + "' ORDER BY questionnum");

		VBox qBox = new VBox();
		qBox.setSpacing(5);

		QuestionBox[] questions = new QuestionBox[numOfQ];

		int i = 0;
		int j = 1;
		while (rs.next()) {
			questions[i] = new QuestionBox(rs.getInt(1), j + ". " + rs.getString(3),
					new String[] { rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8) }, rs.getInt(9));
			qBox.getChildren().add(questions[i]);
			++i;
			++j;
		}
		ScrollPane sp = new ScrollPane(qBox);
		BorderPane root = new BorderPane();

		root.setPrefSize(stageWidth - 10, stageHeight - 40);

		// sp.setPrefSize(stageWidth - 150, stageHeight - 150);
		sp.setMaxHeight(stageHeight - 50);
		sp.setMaxWidth(stageWidth);
		sp.setPadding(new Insets(10));

		Button btnSubmit = new Button("submit");

		// StackPane.setAlignment(sp, Pos.TOP_CENTER);
		root.setCenter(sp);
		root.setBottom(btnSubmit);

		rs = sql.executeQuery("SELECT teacherid, quizname FROM finalproj.quizzes WHERE quizid = '" + quizid + "';");
		rs.next();
		int teacherId = rs.getInt(1);
		String title = rs.getString(2);

		rs = sql.executeQuery("SELECT teachername FROM finalproj.teachers WHERE teacherid = '" + teacherId + "';");
		rs.next();
		String teacherName = rs.getString(1);

		title = title + " by " + teacherName;

		Text titleTxt = new Text(title);
		root.setTop(titleTxt);

		root.setPadding(new Insets(10));

		BorderPane.setAlignment(btnSubmit, Pos.BOTTOM_RIGHT);
		// BorderPane.setAlignment(sp, Pos.CENTER);
		BorderPane.setMargin(titleTxt, new Insets(0));
		BorderPane.setMargin(sp, new Insets(0));

		btnSubmit.setOnAction(e -> {
			try {
				submitQuiz(sql, name, questions, quizid, numOfQ);
			} catch (SQLException e1) {

			}
		});

		return new Scene(root);
	}

	private void submitQuiz(Statement sql, String name, QuestionBox[] questions, int quizid, int numOfQ)
			throws SQLException {
		int numberCorrect = 0;
		System.out.println(questions.length);
		for (int i = 0; i < questions.length; ++i) {
			if (questions[i].isCorrect()) {
				++numberCorrect;
			}
		}

		double percentScore = ((double) numberCorrect / (double) numOfQ) * 100;
		percentScore = round(percentScore, 2);

		sql.executeUpdate("INSERT INTO finalproj.submissions VALUES(DEFAULT, '" + quizid + "', '" + name + "', '"
				+ percentScore + "', '" + numberCorrect + "')");

		stage.setScene(resultsPage(name, percentScore, numberCorrect, numOfQ));
	}

	public static double round(double val, int places) {
		long fact = (long) Math.pow(10, 2);
		val = val * fact;
		long tmp = Math.round(val);
		return (double) tmp / fact;
	}

	private Scene resultsPage(String name, double percentScore, int numberCorrect, int numOfQ) {
		BorderPane root = new BorderPane();
		VBox results = new VBox();
		Button backToHome = new Button("RETURN\nHOME");

		backToHome.setPrefWidth(110);

		results.setPadding(new Insets(100, 100, 0, 100));
		root.setPrefSize(stageWidth, stageHeight);

		results.getChildren().add(new Text("Results for student: " + name + "\nScore/100: " + percentScore
				+ "\nQuestions Correct: " + numberCorrect + "/" + numOfQ));
		root.setCenter(results);
		root.setBottom(backToHome);

		BorderPane.setMargin(backToHome, new Insets(10));
		BorderPane.setAlignment(results, Pos.CENTER);
		BorderPane.setAlignment(backToHome, Pos.BOTTOM_CENTER);

		backToHome.setOnAction(e -> stage.setScene(getStartScene()));

		return new Scene(root);
	}

	class QuestionBox extends VBox {
		private int questionId;

		private int correct;
		private int selected = 0;

		public boolean isCorrect() {
			if (this.correct == this.selected) {
				return true;
			} else {
				return false;
			}
		}

		public int getQuestionId() {
			return questionId;
		}

		public void setQuestionId(int questionId) {
			this.questionId = questionId;
		}

		QuestionBox() {
			this.questionId = -1;
			this.correct = -1;

		}

		QuestionBox(int qId, String qtext, String[] options, int correct) {
			this.questionId = qId;
			this.correct = correct;

			Text qTitle = new Text(qtext);
			qTitle.setStyle("-fx-font-weight: bold");

			/*
			 * Text o1 = new Text(options[0]); Text o2 = new Text(options[1]); Text o3 = new
			 * Text(options[2]); Text o4 = new Text(options[3]);
			 */

			ToggleGroup tg = new ToggleGroup();

			RadioButton o1btn = new RadioButton(options[0]);
			RadioButton o2btn = new RadioButton(options[1]);
			RadioButton o3btn = new RadioButton(options[2]);
			RadioButton o4btn = new RadioButton(options[3]);

			o1btn.setToggleGroup(tg);
			o2btn.setToggleGroup(tg);
			o3btn.setToggleGroup(tg);
			o4btn.setToggleGroup(tg);

			super.getChildren().addAll(qTitle, o1btn, o2btn, o3btn, o4btn);

			tg.selectedToggleProperty().addListener((obserableValue, old_toggle, new_toggle) -> {
				if (o1btn.isSelected()) {
					this.selected = 1;
				} else if (o2btn.isSelected()) {
					this.selected = 2;
				} else if (o3btn.isSelected()) {
					this.selected = 3;
				} else if (o4btn.isSelected()) {
					this.selected = 4;
				}

				// System.out.println(this.selected);
			});

		}
	}

	private boolean isAlphanumeric(String s) {
		return s != null && s.matches("^[a-zA-Z0-9]*$");
	}

	private boolean isLettersAndSpaces(String s) {
		return s != null && s.matches("^[ A-Za-z]+$");
	}

	private String hash256(String pass) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(pass.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
