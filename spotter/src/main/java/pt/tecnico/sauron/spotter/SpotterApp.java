package pt.tecnico.sauron.spotter;

import java.util.Scanner;

public class SpotterApp {

	public static void main(String[] args) {

		System.out.println(SpotterApp.class.getSimpleName());

		// receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		
		// check arguments
		if (args.length < 2) {
			System.out.println("Incorrect ammount of arguments!");
			System.out.printf("Usage: java %s host port%n", SpotterApp.class.getName());
			return;
		}

		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		Spotter spotter = new Spotter(args[0], Integer.parseInt(args[1]));
		String[] arguments;
		String keyword;
		
		// Main cycle
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				keyword = scanner.next();

				switch (keyword) {
					case "spot":
						arguments = scanner.nextLine().split(" ");
						spotter.spot(arguments[0], arguments[1]);
						break;
					case "trail":
						arguments = scanner.nextLine().split(" ");
						spotter.spot(arguments[0], arguments[1]);
						break;
					case "ping":
						spotter.ping();
						break;
					case "clear":
						spotter.clear();
						break;
					case "init":
						spotter.init();
						break;
					case "help":
						spotter.help();
						break;
					case "exit":
						spotter.exit();
						return;
					default:
						System.out.println("Unsupported command: " + keyword);
				}
			}
		}
	}

}
