package room;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class RoomTest {
  @Test
  public void roomModelGetSetTest() {
    RoomModel rm = new RoomModel();

    assertEquals(0, rm.idProperty().get());
    assertEquals(0, rm.bedsProperty().get());
    assertEquals(0, rm.sizeProperty().get());
    assertNull(rm.locationProperty().get());
    assertNull(rm.informationProperty().get());

    rm.setId(1);
    rm.setBeds(10);
    rm.setSize(50);
    rm.setLocation("here");
    rm.setInformation("info");

    assertEquals(1, rm.idProperty().get());
    assertEquals(10, rm.bedsProperty().get());
    assertEquals(50, rm.sizeProperty().get());
    assertEquals("here", rm.locationProperty().get());
    assertEquals("info", rm.informationProperty().get());
  }

  @Test
  public void setRoomValuesTest() {
    Map<String, Object> values = new HashMap<>();
    values.put("id", (Object) 1);
    values.put("beds", (Object) 5);
    values.put("size", (Object) 20);
    values.put("location", (Object) "here");
    values.put("information", (Object) "info");
    RoomModel rm = new RoomModel(values);

    assertEquals(1, rm.idProperty().get());
    assertEquals(5, rm.bedsProperty().get());
    assertEquals(20, rm.sizeProperty().get());
    assertEquals("here", rm.locationProperty().get());
    assertEquals("info", rm.informationProperty().get());
  }
}