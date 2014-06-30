package infrastructure.database.derby;

import com.google.orkut.client.api.ActivityEntry;
import com.google.orkut.client.api.BatchTransaction;
import com.google.orkut.client.api.GetActivitiesTx;
import com.google.orkut.client.api.OrkutAdapter;

public class OrkutDerbyDAO {	
	public static void say(String s) { System.err.println(s); }
	public static void listActivities(OrkutAdapter orkad) throws Exception {
		BatchTransaction btx = orkad.newBatch();
		GetActivitiesTx activities = orkad.getActivityTF().getSelfActivities();
		btx.add(activities);

		say("Getting activities...");
		orkad.submitBatch(btx);
		say("...done");

		int page = 0;
		while (true) {
			say("Page: " + String.valueOf(++page));
			for (int i = 0; i < activities.getActivityCount(); i++) {
				ActivityEntry entry = activities.getActivity(i);
				say(entry.toString());
			}

			if (!activities.hasNext()) break;
			say("Get next page [y/n]? ");
			if (!readline().toLowerCase().startsWith("y")) break;

			activities = orkad.getActivityTF().getNext(activities);
			btx = orkad.newBatch();
			btx.add(activities);
			orkad.submitBatch(btx);
		}
		pause();
	}

	public static String readline() throws Exception {
		java.io.BufferedReader br = new java.io.BufferedReader(
				new java.io.InputStreamReader(
						System.in));
		return br.readLine();
	}

	public static void pause() throws Exception {
		say("Press ENTER to continue.");
		readline();
	}
}