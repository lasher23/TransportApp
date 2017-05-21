package tech.bison.transport;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TransportTest {

	@Test
	public void locations() throws Exception {
		Transport transport = new Transport();
		Stations stations = transport.getStations("Basel");

		Station station = stations.getStations().get(0);
		Coordinate coordinate = station.getCoordinate();
		assertThat(station.getId(), is("000000022"));
		assertThat(station.getName(), is("Basel"));
		assertThat(stations.getStations().get(0).getScore(), is(0));
		assertThat(coordinate.getxCoordinate(), is(47.547408));
		assertThat(coordinate.getyCoordinate(), is(7.589548));
		assertThat(station.getDistance(), is(0.0));
	}

	@Test
	public void stationBoard() throws Exception {
		Transport transport = new Transport();
		StationBoardRoot stationBoard = transport.getStationBoard("Sursee", "8502007");
		assertNotNull(stationBoard);
		assertNotNull(stationBoard.getEntries().get(0).getName());
		assertNotNull(stationBoard.getEntries().get(0).getStop().getDate());
	}

	@Test
	public void connections() throws Exception {
		Transport transport = new Transport();
		Connections connections = transport.getConnections("Sursee", "Luzern");

		assertNotNull(connections.getConnections().get(0).getDuration());
		System.out.println(connections.getConnections().get(0).getTo().getArrivalTimestamp());
		System.out.println(connections.getConnections().get(0).getFrom().getDepartureTimestamp());
	}
}
