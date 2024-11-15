package gettersetter;

 public class User {
String fullname;
String email;
String mobile;

  public String getFullname() {
   return fullname;
  }

  public void setFullname(String fullname) {
   this.fullname = fullname;
  }

  public String getEmail() {
   return email;
  }

  public void setEmail(String email) {
   this.email = email;
  }

  public String getMobile() {
   return mobile;
  }

  public void setMobile(String mobile) {
   this.mobile = mobile;
  }

  public String getOther() {
   return other;
  }

  public void setOther(String other) {
   this.other = other;
  }

  public String getGender() {
   return gender;
  }

  public void setGender(String gender) {
   this.gender = gender;
  }

  String other;
String gender;

     public String getUsertype() {
         return usertype;
     }

     public void setUsertype(String usertype) {
         this.usertype = usertype;
     }

     String usertype;

}

