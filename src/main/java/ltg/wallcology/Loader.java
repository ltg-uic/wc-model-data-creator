package ltg.wallcology;


public class Loader {

	/**
	 * TODO Description
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		DataManager dm = new DataManager();
		dm.loadAndWrite();
		dm.cleanup();
	}

}
