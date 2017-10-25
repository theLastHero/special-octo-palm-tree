package PlayerWarpGUI.Commands;

public enum PWOSubCommandType {
	
	    HELP,
	    SET,
	    DELETE,
	    SETICON,
	    SETTITLE,
	    SETLORE,
	    BAN,
	    UNBAN,
	    SHOW,
	    LIST;

	    public static PWOSubCommandType getSubcommand(String commandName) {
	        for (PWOSubCommandType command : values()) {
	            if (command.name().equalsIgnoreCase(commandName)) {
	                return command;
	            }
	        }

	        if (commandName.equalsIgnoreCase("?") || commandName.equalsIgnoreCase("help")) {
	            return HELP;
	        }
	        else if (commandName.equalsIgnoreCase("SET") || commandName.equalsIgnoreCase("setwarp")) {
	            return SET;
	        }
	        else if (commandName.equalsIgnoreCase("delete") || commandName.equalsIgnoreCase("deletewarp")) {
	            return DELETE;
	        }
	        else if (commandName.equalsIgnoreCase("seticon")) {
	            return SETICON;
	        }
	        else if (commandName.equalsIgnoreCase("settitle")) {
	            return SETTITLE;
	        }
	        else if (commandName.contains("setlore")) {
	            return SETLORE;
	        }
	        else if (commandName.equalsIgnoreCase("ban")) {
	            return BAN;
	        }
	        else if (commandName.equalsIgnoreCase("unban")) {
	            return UNBAN;
	        }
	        else if (commandName.equalsIgnoreCase("show")) {
	            return SHOW;
	        }
	        else if (commandName.equalsIgnoreCase("list")) {
	            return LIST;
	        }

	        return null;
	}

}
