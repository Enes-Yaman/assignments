public class User {
    boolean Clup, Admin;
    String Name, Password;
    String Answer;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public void setPassword(String password) {
        Password = password;
    }

    String Question;

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    User(String name, String password, boolean isitClupMember, boolean isitAdmin) {
        this.Admin = isitAdmin;
        this.Clup = isitClupMember;
        this.Name = name;
        this.Password = password;
    }
    User(String name, String password, boolean isitClupMember, boolean isitAdmin, String answer) {
        this.Admin = isitAdmin;
        this.Clup = isitClupMember;
        this.Name = name;
        this.Password = password;
        this.Answer = answer;
    }

    public void setClup(boolean clup) {
        Clup = clup;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }

    public boolean getClup() {
        return Clup;
    }

    public boolean getAdmin() {
        return Admin;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }
}
