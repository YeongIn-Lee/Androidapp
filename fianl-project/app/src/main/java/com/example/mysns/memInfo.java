package com.example.mysns;

public class memInfo {

        private String name;
        private String date;
        private String phone;
        private String address;

        public memInfo(){}

        public memInfo(String name, String date, String phone, String address) {
           this.name=name;
           this.date =date;
           this.phone = phone;
           this.address = address;
        }

        public String getName() {
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date){
            this.date = date;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone){
             this.phone = phone;
          }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address){
            this.address = address;
        }
    }


