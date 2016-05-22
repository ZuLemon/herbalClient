package net.andy.dispensing.util;

public class SpinnerItem {
    private Integer ID = 0;
     private String Value = "";

     public SpinnerItem () {
      ID = 0;
      Value = "";
     }

     public SpinnerItem (Integer _ID, String _Value) {
      ID = _ID;
      Value = _Value;
     }

     @Override
     public String toString() {
      return Value;
     }

     public Integer GetID() {
      return ID;
     }

     public String GetValue() {
      return Value;
     }
}