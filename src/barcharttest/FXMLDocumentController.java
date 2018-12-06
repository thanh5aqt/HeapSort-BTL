package barcharttest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

public class FXMLDocumentController implements Initializable {

	@FXML
	private Label label;

	@FXML
	private ComboBox<String> comboBox;

	@FXML
	private Slider slider;

	@FXML
	private TextField textField;

	@FXML
	private Button button;

	@FXML
	private Button button2;

	@FXML
	private BarChart<String, Number> chart;

	private ArrayList<XYChart.Data<String, Number>> bars;

	private static final String COLOR_COMPARING = "-fx-bar-fill: #00ffff",
			COLOR_SWAPPED = "-fx-bar-fill: #ff00e2";

	static int BAR_COUNT = 15;

	static int INIT_VALUE = 1000;

	ObservableList<String> list2 = FXCollections.observableArrayList(
			"Random array", "Few unique array", "Almost sorted array", "Reversed array"
	);

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		comboBox.setItems(list2);
		comboBox.setValue("Random array");

		slider.setValue(INIT_VALUE);
		slider.valueProperty().addListener((obs, oldval, newVal)
				-> slider.setValue(Math.round(newVal.doubleValue())));

		textField.setText(String.valueOf(INIT_VALUE));
		textField.textProperty().bindBidirectional(slider.valueProperty(), new NumberStringConverter());

		button.setOnAction(event -> new Thread(() -> {
			if (comboBox.getValue() != null) {
				disableUI(true);

				heapSort();

				disableUI(false);
			}
		}).start());

		refreshBarChart();

		paintHeap();

		Platform.runLater(() -> randomArray());
	}

	//Event handler
	//--------------------------------------------------------------------------
	public void refreshBarChart() {
		bars = new ArrayList<XYChart.Data<String, Number>>();
		final XYChart.Series<String, Number> data = new XYChart.Series<>();
		chart.getData().add(data);
		for (int i = 0; i < BAR_COUNT; ++i) {
			bars.add(new XYChart.Data<>(i + "", i + 1));
			data.getData().add(bars.get(i));
		}
	}

	public void handleComboBoxAction(ActionEvent event) {
		if (comboBox.getValue() == "Random array") {
			Platform.runLater(() -> randomArray());
		} else if (comboBox.getValue() == "Few unique array") {
			Platform.runLater(() -> fewUniqueArray());
		} else if (comboBox.getValue() == "Almost sorted array") {
			Platform.runLater(() -> almostSortedArray());
		} else if (comboBox.getValue() == "Reversed array") {
			Platform.runLater(() -> reversedArray());
		}
	}

	public void handleGenerateAction(ActionEvent event) {
		if (comboBox.getValue() == "Random array") {
			Platform.runLater(() -> randomArray());
		} else if (comboBox.getValue() == "Few unique array") {
			Platform.runLater(() -> fewUniqueArray());
		} else if (comboBox.getValue() == "Almost sorted array") {
			Platform.runLater(() -> almostSortedArray());
		} else if (comboBox.getValue() == "Reversed array") {
			Platform.runLater(() -> reversedArray());
		}

		paintHeap();
	}

	public void changeLabelText(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
	}

	public void disableUI(boolean isDisable) {
		button.setDisable(isDisable);
		button2.setDisable(isDisable);
		slider.setDisable(isDisable);
		textField.setDisable(isDisable);
		comboBox.setDisable(isDisable);
	}

	public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//--------------------------------------------------------------------------

	//Bar data getter and setter
	//--------------------------------------------------------------------------
	public void setBar(int index, int value) {
		bars.get(index).setYValue(value);
	}

	public int getBar(int index) {
		return (int) bars.get(index).getYValue();
	}
	//--------------------------------------------------------------------------

	//Fill color
	//--------------------------------------------------------------------------
	public void paint(int index, String style) {
		Platform.runLater(() -> {
			bars.get(index).getNode().setStyle(style);
		});
	}

	public void paintHeap() {
		paint(0, "-fx-bar-fill: #ff0000");

		paint(1, "-fx-bar-fill: #ff7f00");
		paint(2, "-fx-bar-fill: #ff7f00");

		for (int i = 3; i < 7; ++i) {
			paint(i, "-fx-bar-fill: #ffff00");
		}

		for (int i = 7; i < 15; ++i) {
			paint(i, "-fx-bar-fill: #00ff00");
		}

		for (int i = 15; i < 31; ++i) {
			paint(i, "-fx-bar-fill: #0000ff");
		}
	}
	//--------------------------------------------------------------------------

	//Array type
	//--------------------------------------------------------------------------
	public void randomArray() {
		Random rd = new Random();

		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, i + 1);
		}

		for (int i = 1; i < BAR_COUNT; ++i) {
			int j = rd.nextInt(i);

			int temp = getBar(i);
			setBar(i, getBar(j));
			setBar(j, temp);
		}

		paintHeap();
	}

	public void fewUniqueArray() {
		Random rd = new Random();

		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, ((i % 5) + 1) * (BAR_COUNT / 5));
		}

		for (int i = 1; i < BAR_COUNT; ++i) {
			int j = rd.nextInt(i);

			int temp = getBar(i);
			setBar(i, getBar(j));
			setBar(j, temp);
		}

		paintHeap();
	}

	public void almostSortedArray() {
		randomArray();
		int[] gaps = {701, 301, 132, 57, 23, 10, 4};

		for (int gap : gaps) {
			for (int i = gap; i < BAR_COUNT; ++i) {
				int key = getBar(i);

				int j = i;

				while (j >= gap && key < getBar(j - gap)) {
					int temp = getBar(j);
					setBar(j, getBar(j - gap));
					setBar(j - gap, temp);

					j -= gap;
				}
			}
		}
	}

	public void reversedArray() {
		for (int i = 0; i < BAR_COUNT; ++i) {
			setBar(i, BAR_COUNT - i);
		}

		paintHeap();
	}
	//--------------------------------------------------------------------------

	//Sorting algorithm
	//--------------------------------------------------------------------------
	public void heapify(int i, int n) {
		int left = 2 * i + 1;
		int right = 2 * i + 2;

		int max = i;
		
		paint(i, COLOR_COMPARING);
		paint(left, COLOR_COMPARING);
		delay((int) slider.getValue());
		paintHeap();

		paint(i, COLOR_COMPARING);
		paint(right, COLOR_COMPARING);
		delay((int) slider.getValue());
		paintHeap();

		if ((left < n) && (getBar(left) > getBar(max))) {
			max = left;
		}

		if ((right < n) && (getBar(right) > getBar(max))) {
			max = right;
		}

		if (max != i) {
			paint(i, COLOR_SWAPPED);
			paint(max, COLOR_SWAPPED);
			delay((int) slider.getValue());
			paintHeap();

			int temp = getBar(i);
			setBar(i, getBar(max));
			setBar(max, temp);

			heapify(max, n);
		}
	}

	public void heapSort() {
		changeLabelText("Initialize heap");

		for (int i = BAR_COUNT / 2 - 1; i >= 0; --i) {
			heapify(i, BAR_COUNT);
		}

		for (int i = BAR_COUNT - 1; i >= 0; --i) {
			int temp = getBar(0);
			setBar(0, getBar(i));
			setBar(i, temp);

			changeLabelText("Swap arr[0] with arr[" + i + "]");

			paint(0, COLOR_SWAPPED);
			paint(i, COLOR_SWAPPED);
			delay((int) slider.getValue());
			paintHeap();

			changeLabelText("Recreat heap with new root");

			heapify(0, i);
		}

		changeLabelText("Done");
	}
	//--------------------------------------------------------------------------
}
