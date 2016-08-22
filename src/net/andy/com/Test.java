//package net.andy.com;
//
//import net.andy.boiling.domain.Users;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.Date;
//import java.util.Objects;
//
///**
// * Created by Guang on 2016/8/17.
// */
//public class Test {
//    public static void main(String args[]) throws Exception {
//        Users users=new Users();
//        users.setId(4);
//        users.setUname("asdadwq");
////        getObjectValue(users);
//        Field[] fields=users.getClass().getDeclaredFields();
//            //遍历
//        for(Field field:fields){
//            field.setAccessible(true);
//            //获取一个属性
//            String name = field.getName();
//            System.out.print(name);
//            //获取该属性的值，可能为空
//            Method m = (Method) users.getClass().getMethod(
//                    "get" + getMethodName(field.getName()));
//            Object val =  m.invoke(users);// 调用getter方法获取属性值
//            if (val != null) {
//                System.out.println("" + val);
//            }
//        }
//
//    }
//    public static void getObjectValue(Object object) throws Exception {
//
//        if (object != null) {
//            // 拿到该类
//            Class<?> clz = object.getClass();
//            // 获取实体类的所有属性，返回Field数组
//            Field[] fields = clz.getDeclaredFields();
//            for (Field field : fields) {// --for() begin
//                field.setAccessible(true);
//                //获取一个属性
//                String name = field.getName();
//                System.out.print(name);
//                // 如果类型是String
//                if (field.getGenericType().toString().equals(
//                        "class java.lang.String")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    String val = (String) m.invoke(object);// 调用getter方法获取属性值
//                    if (val != null) {
//                        System.out.println("String type:" + val);
//                    }
//                }
//                // 如果类型是Integer
//                if (field.getGenericType().toString().equals(
//                        "class java.lang.Integer")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    Integer val = (Integer) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("Integer type:" + val);
//                    }
//                }
//                // 如果类型是Double
//                if (field.getGenericType().toString().equals(
//                        "class java.lang.Double")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    Double val = (Double) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("Double type:" + val);
//                    }
//                }
//                // 如果类型是Boolean 是封装类
//                if (field.getGenericType().toString().equals(
//                        "class java.lang.Boolean")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            field.getName());
//                    Boolean val = (Boolean) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("Boolean type:" + val);
//                    }
//                }
//                // 反射找不到getter的具体名
//                if (field.getGenericType().toString().equals("boolean")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            field.getName());
//                    Boolean val = (Boolean) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("boolean type:" + val);
//                    }
//                }
//                // 如果类型是Date
//                if (field.getGenericType().toString().equals(
//                        "class java.util.Date")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    Date val = (Date) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("Date type:" + val);
//                    }
//                }
//                // 如果类型是Short
//                if (field.getGenericType().toString().equals(
//                        "class java.lang.Short")) {
//                    Method m = (Method) object.getClass().getMethod(
//                            "get" + getMethodName(field.getName()));
//                    Short val = (Short) m.invoke(object);
//                    if (val != null) {
//                        System.out.println("Short type:" + val);
//                    }
//                }
//            }
//
//        }
//    }
//    // 把一个字符串的第一个字母大写、效率是最高的、
//    private static String getMethodName(String fildeName) throws Exception{
//        byte[] items = fildeName.getBytes();
//        items[0] = (byte) ((char) items[0] - 'a' + 'A');
//        return new String(items);
//    }
//}
