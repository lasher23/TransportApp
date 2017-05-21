package tech.bison.transportapp.view.searchconnections;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.bison.transport.Connection;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Section;
import tech.bison.transport.Transport;
import tech.bison.utils.FXUtils;
import tech.bison.utils.FXUtils.AutoCompleteComparator;

public class SearchConnectionController {
	@FXML
	private ComboBox<String> inputStart;
	@FXML
	private ComboBox<String> inputDestination;
	@FXML
	private TableView<Connection> tableView;
	@FXML
	private TableColumn<Connection, String> columnBus;
	@FXML
	private TableColumn<Connection, String> columnDestination;
	@FXML
	private TableColumn<Connection, String> columnDepartureTime;
	@FXML
	private TableColumn<Connection, String> columnArrivalTime;
	private Transport transport = new Transport();
	private ObservableList<Connection> connections = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		TextField editorStart = inputStart.getEditor();
		editorStart.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue) {
				inputStart.getItems().clear();
				inputStart.getItems().add(editorStart.getText());
				inputStart.getSelectionModel().select(editorStart.getText());
			}
		});

		TextField editorDestination = inputDestination.getEditor();
		editorDestination.focusedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (!newValue) {
				inputDestination.getItems().clear();
				inputDestination.getItems().add(editorStart.getText());
				inputDestination.getSelectionModel().select(editorStart.getText());
			}
		});
		inputStart.getItems().add("Test");
		FXUtils.autoCompleteComboBoxPlus(inputStart, new AutoCompleteComparator<String>() {

			@Override
			public boolean matches(String typedText, String objectToCompare) {
				return true;
			}
		});

		columnBus.setCellValueFactory(cellData -> {
			StringBuilder sb = new StringBuilder();
			for (Section section : cellData.getValue().getSections()) {
				try {
					sb.append(section.getJourney().getName() + "\n");
				} catch (NullPointerException e) {
					// somtimes the api returns null
				}
			}
			return new SimpleStringProperty(sb.toString().substring(0, sb.length() - 2));
		});
		columnDestination.setCellValueFactory(cellData -> {
			System.out.println(cellData.getValue().getFrom().getDepartureTimestamp());
			return new SimpleStringProperty(cellData.getValue().getTo().getStation().getName());
		});
		columnDepartureTime.setCellValueFactory(cellData -> new SimpleStringProperty(
				convertTimestampToStringFormat(Long.parseLong(cellData.getValue().getFrom().getDepartureTimestamp()))));
		columnArrivalTime.setCellValueFactory(cellData -> new SimpleStringProperty(
				convertTimestampToStringFormat(Long.parseLong(cellData.getValue().getTo().getArrivalTimestamp()))));
		// TODO
		tableView.setItems(connections);
	}

	@FXML
	private void onSearchClick() {
		try {
			connections.clear();
			connections.addAll(transport.getConnections(inputStart.getSelectionModel().getSelectedItem(),
					inputDestination.getSelectionModel().getSelectedItem()).getConnections());
			tableView.refresh();
		} catch (PublicTransportServiceUnvailableException e) {
			showNoConnectionAlert();
		}
	}

	private void showNoConnectionAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Fehler bei der Verbindun zum Server!");
		alert.setContentText("Prüfen sie ihre Internetverbindung und versuchen sie es später nochmal.");
		alert.show();
	}

	private String convertTimestampToStringFormat(long timestamp) {
		LocalDateTime dateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
		String date = dateTime.format(DateTimeFormatter.ofPattern("hh:mm dd.MM.yyyy"));
		return date;
	}

}
