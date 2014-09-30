package core;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DBConnectorTest {
	DBConnector dbc;

	@Before
	public void setUp() throws Exception {
		dbc = new DBConnector(new Core(), 
				Core.DBhostAddress, Core.DBUserName, Core.DBPassword);
	}

	@Test
	public void testDBConnector() {
		assertNotEquals(dbc.tables.size(), 0);
		assertNotEquals(dbc.tableColumns.size(), 0);
		
		for (List<String> columns : dbc.tableColumns) {
			assertNotEquals(columns.size(), 0);			
		}
	}

	@Test
	public void testGetQuery() {
		ResultSet res;
		
		for (String table : dbc.tables) {
			res = dbc.getQuery(table);

			try {
				assertTrue(res.isBeforeFirst() || !res.next());
				assertFalse(res.isClosed());
			
				res.close();
				assertTrue(res.isClosed());
				
			} catch (SQLException e) {
				assertTrue(false);
			}
		}
	}

	@Test
	public void testInsertRow() throws SQLException {
		ResultSet res;
		
		List<Object> newRow = new ArrayList<Object>();
		
		for (String table : dbc.tables) {
			
			res = dbc.getQuery(table);
		
			ResultSetMetaData resMeta = res.getMetaData();
			int columnCount = resMeta.getColumnCount();				
			
			boolean oneColumnWritable = false;
			
			res.moveToInsertRow();
			
			for (int columnI = 1; columnI <= columnCount; columnI++) {
				if (resMeta.isDefinitelyWritable(columnI)) {
					oneColumnWritable = true;
				
					Object newField;
					switch (resMeta.getColumnTypeName(columnI)) {
						case "VARCHAR":
							newField = "TEST";
							res.updateString(columnI, (String) newField);
							break;
						case "INT":
							newField = 20;
							res.updateInt(columnI, (Integer) newField);
							break;
						case "TINYINT":
							newField = 20;
							res.updateInt(columnI, (Integer) newField);
							break;
						case "FLOAT":
							newField = 20.0;
							res.updateDouble(columnI, (Double) newField);
							break;
						default:
							newField = null;
							res.updateNull(columnI);
							break;
					}
					newRow.add(newField);
				}
			}
			res.insertRow();
			
			dbc.insertRow(table, newRow);
			ResultSet res2 = dbc.getQuery(table);
			
			assertTrue(res.equals(res2)); // Det er ikke sikkert at denne fungerer til å sammenligne de to ResultSet-ene. I så fall må hver celle sammenlignes manuelt. -Sindre
			assertTrue(oneColumnWritable);

			res.close();
			res2.close();
	
		}
	}

	@Test
	public void testDeleteRow() throws SQLException {
		for (String table : dbc.tables) {
			
			ResultSet res = dbc.getQuery(table);
			res.last();
			int oldRowCount = res.getRow();
			
			res.close();
			dbc.deleteRow(table, oldRowCount);
			
			res = dbc.getQuery(table);
			res.last();
			
			assertEquals(res.getRow(), oldRowCount);	
		}
	}

	@Test
	public void testEditRow() {
		fail("Not yet implemented");
	}

}
