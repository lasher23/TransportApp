package tech.bison.transportapp.view.searchconnections;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.controlsfx.control.textfield.TextFields;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import tech.bison.transport.Connection;
import tech.bison.transport.PublicTransportServiceUnvailableException;
import tech.bison.transport.Section;
import tech.bison.transport.Station;
import tech.bison.transport.Transport;

public class SearchConnectionController {

	@FXML
	private TextField txtStart;
	@FXML
	private TextField txtDestination;
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
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	private Transport transport = new Transport();
	private ObservableList<Connection> connections = FXCollections.observableArrayList();

	@FXML
	private void initialize() {

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
		columnDestination.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getTo().getStation().getName()));
		columnDepartureTime.setCellValueFactory(cellData -> new SimpleStringProperty(
				convertTimestampToStringFormat(Long.parseLong(cellData.getValue().getFrom().getDepartureTimestamp()))));
		columnArrivalTime.setCellValueFactory(cellData -> new SimpleStringProperty(
				convertTimestampToStringFormat(Long.parseLong(cellData.getValue().getTo().getArrivalTimestamp()))));
		setAutocompleteToTextField(txtStart);
		setAutocompleteToTextField(txtDestination);
		tableView.setItems(connections);
	}

	@FXML
	private void onSearchClick() {
		try {
			connections.clear();
			connections.addAll(transport.getConnections(txtStart.getText(), txtDestination.getText()).getConnections());
			tableView.refresh();
			System.out.println(
					dateFormat.format(new Date(Long.parseLong(connections.get(0).getFrom().getDepartureTimestamp()))));
		} catch (PublicTransportServiceUnvailableException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Fehler bei der Verbindun zum Server!");
			alert.setContentText("Prüfen sie ihre Internetverbindung und versuchen sie es später nochmal.");
			alert.show();
		}
	}

	private void setAutocompleteToTextField(TextField textField) {
		textField.textProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
			try {
				List<Station> stations = transport.getStations(newValue).getStations();
				stations.sort((o1, o2) -> {
					if (o1.getScore() <= o2.getScore()) {
						return -1;
					} else if (o1.getScore() >= o2.getScore()) {
						return 1;
					}
					return 0;
				});
				List<String> names = new ArrayList<>();
				int i = 0;
				for (Station station : stations) {
					// only show 10
					if (i > 10) {
						break;
					}
					names.add(station.getName());
					i++;
				}
				TextFields.bindAutoCompletion(textField, names);
			} catch (PublicTransportServiceUnvailableException e1) {
				// To Many request next char it will get reloaded
				// TextFields.bindAutoCompletion(txtStart, "");
			}
		});
	}

	private String convertTimestampToStringFormat(long timestamp) {
		return dateFormat.format(new Date(timestamp));
	}
}
