package ca.robinssoftware.nationstates;

import java.util.List;

import org.bukkit.command.CommandSender;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

public class PagedChatMessage {

    private List<String> entries;
    private String title;
    
    private String footer = PLUGIN.getLanguageData().get("DEFAULT_FOOTER");
    private int entriesPerPage = 8;

    public PagedChatMessage(String title, List<String> entries) {
        this.title = title;
        this.entries = entries;
    }

    public int getPages() {
        return (int) (Math.ceil((double) entries.size() / entriesPerPage));
    }
    
    public PagedChatMessage addEntry(String string) {
        entries.add(string);
        return this;
    }
    
    public PagedChatMessage addEntries(List<String> strings) {
        entries.addAll(strings);
        return this;
    }

    public PagedChatMessage removeEntry(String string) {
        entries.remove(string);
        return this;
    }

    public PagedChatMessage removeEntry(int entry) {
        entries.remove(entry);
        return this;
    }

    public PagedChatMessage setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public PagedChatMessage setEntriesPerPage(int entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
        return this;
    }
    
    public PagedChatMessage setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    public PagedChatMessage sendTo(CommandSender sender, int page) {
        sender.sendMessage(title);
        
        for (int i = 0; i < entriesPerPage; i++) {
            if (entries.size() > page * entriesPerPage + i)
                sender.sendMessage(entries.get(page * entriesPerPage + i));
        }
        
        sender.sendMessage(footer.replaceFirst("%s", (page + 1) + "").replaceFirst("%s", getPages() + ""));
        return this;
    }

}
