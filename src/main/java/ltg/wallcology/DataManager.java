package ltg.wallcology;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {
	
	private DataInputStream in = null;
	private BufferedReader data = null;
	
	//DB connection
	private Connection conn = null;
	
	
	public DataManager() {
		// Setup file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("src/main/resources/wc_experiment_data.txt");
			in = new DataInputStream(fstream);
			data = new BufferedReader(new InputStreamReader(in)); 
		} catch (FileNotFoundException e) {
			System.out.println("File is not acessible!!!");
			System.exit(-1);
		}
		// Setup DB
		try {
			conn = DriverManager.getConnection("jdbc:mysql://carbon.evl.uic.edu/" +
					"wallcology_microworlds?" +
					"user=wallcology&" +
					"password=CAT.evl");
		} catch (SQLException e) {
			System.out.println("Impossible to open DB connection");
			System.exit(-1);
		}
		//System.out.println("File is open and we are connected to DB");
	}
	
	
	public void loadAndWrite() {
		// Create table - EXECUTE ONCE!!!!
		//alterTable();		
		// Add all experiments to experiments table - EXECUTE ONCE!!!!
		//addExperiments();
		//addPredatorExperiments();
		// Create the prepared statement to insert 
		PreparedStatement ps = prepareInsertStatement();
		// Read file line after line and insert in the DB
		readDataFileAndWrite(ps);
	}
	
	
///// WRITE DATA INTO DATA TABLE

	
	private void readDataFileAndWrite(PreparedStatement ps) {
		String line = null;
		String[] values;
		int n = 0;
		do {
			try {
				line = data.readLine();
				if (line==null)
					break;
				values = line.split(",");
				if(values[0].equals("0"))
					continue;
				//System.out.println("Line contains " + values.length + " dps");
				for (int i=0; i<values.length; i++) {
					ps.setInt(i+1, Integer.parseInt(values[i]));
				}
				ps.executeUpdate();
				n++;
			} catch (IOException e) {
				System.out.println("Can't read line!!!");
				return;
			} catch (SQLException e) {
				System.out.println("Unable to write data in DB!!!");
				return;
			}
		} while (line != null);
		System.out.println("Done! Added " + n + " rows!");
	} 
	
	
	
	private PreparedStatement prepareInsertStatement() {
		String sqlstm = "INSERT INTO data (dp0 ";
		String qm = "(?";
		for (int i=1; i<100; i++) {
			sqlstm += ", dp" + i;
			qm+= ",?";
		}
		sqlstm += ") VALUES " +qm+")";
		//System.out.println(sqlstm);
		try {
			return conn.prepareStatement(sqlstm);
		} catch (SQLException e1) {
			System.out.println("Can't prepare statement!!!");
			System.exit(-1);
		}
		return null;
	}

	
	
	///// ADD PREDATOR EXPERIMENTS
	
	@SuppressWarnings("unused")
	private void addPredatorExperiments() {
		addPredatorExperiment(1, 1, 1);
		addPredatorExperiment(0, 0, 0);
		addPredatorExperiment(0, 0, 1);
		addPredatorExperiment(0, 1, 0);
		addPredatorExperiment(0, 1, 1);
		addPredatorExperiment(1, 0, 0);
		addPredatorExperiment(1, 0, 1);
		addPredatorExperiment(1, 1, 0);
	}
	
	
	
	private void addPredatorExperiment(int t, int l, int h) {
		String oc = "";
		String ov = "";
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			System.out.println("Can't prepare statement!!!");
			System.exit(-1);
		}
		
		// 1 Just Predator (1)
		oc = "predator"; 
		ov = "10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 1");
		}
		
		// 2 Scum and Predator (2)
		oc = "scum, predator"; 
		ov = "72, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 2");
		}
		
		// 3 Fuzz and Predator (2)
		oc = "fuzz, predator"; 
		ov = "63, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 3");
		}
		
		// 4 Scum Eaters and Predator (2)
		oc = "scum_eaters, predator"; 
		ov = "20, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 4");
		}
		
		// 5 Fuzz Eaters and Predator (2)
		oc = "fuzz_eaters, predator"; 
		ov = "15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 5");
		}
		
		// 6 Scum, Fuzz and Predator (3)
		oc = "scum, fuzz, predator"; 
		ov = "72, 63, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 6");
		}
		
		// 7 SE, FE and Predator (3)
		oc = "scum_eaters, fuzz_eaters, predator"; 
		ov = "20, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 7");
		}

		// 8 Scum, SE and Predator (3)
		oc = "scum, scum_eaters, predator"; 
		ov = "72, 20, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 8");
		}

		// 9 Fuzz, FE and Predator (3)
		oc = "fuzz, fuzz_eaters, predator"; 
		ov = "63, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 9");
		}

		//10 Scum, FE and Predator (3)
		oc = "scum, fuzz_eaters, predator"; 
		ov = "72, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 10");
		}

		//11 Fuzz, SE and Predator (3)
		oc = "fuzz, scum_eaters, predator"; 
		ov = "63, 20, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 11");
		}
		
		//12 Scum, Fuzz, SE and Predator (4)
		oc = "scum, fuzz, scum_eaters, predator"; 
		ov = "72, 63, 20, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 12");
		}
		
		//13 Scum, Fuzz, FE and Predator (4)
		oc = "scum, fuzz, fuzz_eaters, predator"; 
		ov = "72, 63, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 13");
		}
		
		//14 Scum, SE, FE, and Predator (4)
		oc = "scum, scum_eaters, fuzz_eaters, predator"; 
		ov = "72, 20, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 14");
		}
		
		//15 Fuzz, SE, FE and Predator (4)
		oc = "fuzz, scum_eaters, fuzz_eaters, predator"; 
		ov = "63, 20, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 15");
		}
		
		//16 Complete systems with 5 creatures (5)
		oc = "scum, fuzz, scum_eaters, fuzz_eaters, predator"; 
		ov = "72, 63, 20, 15, 10";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 16");
		}
	}
	
	
	///// ADD INDIRECT MUTUALISM EXPERIMENTS

	@SuppressWarnings("unused")
	private void addExperiments() {
		addExperiment(0, 0, 0);
		addExperiment(0, 0, 1);
		addExperiment(0, 1, 0);
		addExperiment(0, 1, 1);
		addExperiment(1, 0, 0);
		addExperiment(1, 0, 1);
		addExperiment(1, 1, 0);
	}
	
	private void addExperiment(int t, int l, int h) {
		String oc = "";
		String ov = "";
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e) {
			System.out.println("Can't prepare statement!!!");
			System.exit(-1);
		}
		
		// 1 Just Scum
		oc = "scum"; 
		ov = "72";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 1");
		}
		
		// 2 Just Fuzz
		oc = "fuzz"; 
		ov = "63";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 2");
		}
		
		// 3 Scum and Fuzz
		oc = "scum, fuzz"; 
		ov = "72, 63";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 3");
		}
		
		// 4 Just SE
		oc = "scum_eaters"; 
		ov = "20";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 4");
		}
		
		// 5 Just FE
		oc = "fuzz_eaters"; 
		ov = "15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 5");
		}
		
		// 6 SE and FE
		oc = "scum_eaters, fuzz_eaters"; 
		ov = "20, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 6");
		}
		
		// 7 Scum and SE
		oc = "scum, scum_eaters"; 
		ov = "72, 20";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 7");
		}
		
		// 8 Fuzz and FE
		oc = "fuzz, fuzz_eaters"; 
		ov = "63, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 8");
		}
		
		// 9 Scum and FE
		oc = "scum, fuzz_eaters"; 
		ov = "72, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 9");
		}
		
		//10 Fuzz and SE
		oc = "fuzz, scum_eaters"; 
		ov = "63, 20";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 10");
		}
		
		//11 S, F and SE
		oc = "scum, fuzz, scum_eaters"; 
		ov = "72, 63, 20";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 11");
		}
		
		//12 S, F and FE
		oc = "scum, fuzz, fuzz_eaters"; 
		ov = "72, 63, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 12");
		}
		
		//13 S, SE and FE
		oc = "scum, scum_eaters, fuzz_eaters"; 
		ov = "72, 20, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 13");
		}
		
		//14 F, SE and FE
		oc = "fuzz, scum_eaters, fuzz_eaters"; 
		ov = "63, 20, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 14");
		}
		
		//15 Complete system
		oc = "scum, fuzz, scum_eaters, fuzz_eaters"; 
		ov = "72, 63, 20, 15";
		try {
			stm.executeUpdate(buildStm(oc, ov, t, l, h));
		} catch (SQLException e) {
			System.out.println("Can't execute 15");
		}
	}


	private String buildStm(String oc, String ov, int t, int l, int h) {
		String columns = null;
		String values = null;
		columns = "INSERT INTO experiments (temperature, light, humidity, " + oc + ") " ;
		values = "VALUES (" + t + ", " + l + ", " + h + ", " + ov + ")";
		return columns + values;
	}
	
	
	

	
	
	///// CREATE DATA TABLE 

	@SuppressWarnings("unused")
	private void alterTable() {
		Statement statement = null;
		String st = "ALTER TABLE data ";
		for (int i=0; i<100; i++) 
			st += "ADD COLUMN dp" + i + " INT NULL, ";
		st = st.substring(0, st.length()-2);
		System.out.println(st);
		try {
			statement = conn.createStatement();
			statement.executeUpdate(st);
		} catch (SQLException e1) {
			System.out.println("Impossible to change table!!!");
		}	
	}


	public void cleanup() {
		try {
			in.close();
			conn.close();
		} catch (IOException e) {
			System.out.println("Can't even close file...");
		} catch (SQLException e) {
			System.out.println("Can't even close DB connection...");		
		}
	}

}
