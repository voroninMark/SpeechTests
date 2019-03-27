package com.example.speechtests.DO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
        private int id;
        private String pseudo;
        private String mdp;
        private String mail;

        public User() {
        }

        public User(JSONObject json) throws JSONException {
            id = json.getInt("id");
            pseudo = json.getString("nick");
            mdp = json.getString("passwd");
            mail = json.getString("email");
        }
        public User(String _pseudo,String _mdp, String _mail) {
            pseudo=_pseudo;
            mdp=_mdp;
            mail=_mail;
        }
        public User(Parcel in) {
            id=in.readInt();
            pseudo=in.readString();
            mdp=in.readString();
            mail=in.readString();
        }
        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(pseudo);
            dest.writeString(mdp);
            dest.writeString(mail);
        }

        public String getPseudo() {
            return pseudo;
        }

        public void setPseudo(String pseudo) {
            this.pseudo = pseudo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMdp() {
            return mdp;
        }

        public void setMdp(String mdp) {
            this.mdp = mdp;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", pseudo='" + pseudo + '\'' +
                    ", mdp='" + mdp + '\'' +
                    ", mail='"+ mail + '\''+
                    '}';
        }

        public JSONObject toJson() throws JSONException {
            JSONObject jsonUser = new JSONObject();

            jsonUser.put("api_nick", "laurent");
            jsonUser.put("api_passwd", "api_vocacook_access_0");
            jsonUser.put("nick", this.pseudo);
            jsonUser.put("passwd", this.mdp);
            jsonUser.put("email", this.mail);

            return  jsonUser;
        }
}
