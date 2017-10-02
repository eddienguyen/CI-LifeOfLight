package novels;

import java.util.Arrays;
import java.util.List;

public class Story {
    public String id, text;
    public List<Choice> choices;

    public Story(String id, String text, Choice... choices) {        //"..." refer to varargs method
        this.id = id;
        this.text = text;
        this.choices = Arrays.asList(choices);                      //convert Array of choices to List of choices
    }
}
