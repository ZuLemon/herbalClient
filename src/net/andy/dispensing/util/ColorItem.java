package net.andy.dispensing.util;

public class ColorItem {
    private String color = "";
     private String value = "";

 public ColorItem(String color, String value) {
  this.color = color;
  this.value = value;
 }

 public String getColor() {
  return color;
 }

 public void setColor(String color) {
  this.color = color;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }
 public String toString(){
  return color;
 }
}