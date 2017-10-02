package novels;

public class Choice {
    public String value;
    public String to;

    public Choice(String value, String to) {
        this.value = value;
        this.to = to;
    }

    public boolean match(String answer){
        return answer.equalsIgnoreCase(value) /*|| answer.equalsIgnoreCase("other")*/;
    }
}
