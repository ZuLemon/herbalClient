//import net.andy.boiling.domain.PrescriptionDetailDomain;
//import net.andy.com.Http;
//
//import java.util.*;
//
///**
// * Created by Guang on 2016/3/2.
// */
//public class ListMapTest {
//
//    public static void main(String args[]){
////        List<PrescriptionDetailDomain> detailDomainList=new ArrayList<>();
////        PrescriptionDetailDomain pre1=new PrescriptionDetailDomain();
////        pre1.setId(10);
////        pre1.setHerbName("陈皮");
////        pre1.setQuantity(3);
////        detailDomainList.add(pre1);
////        PrescriptionDetailDomain pre2=new PrescriptionDetailDomain();
////        pre2.setId(12);
////        pre2.setHerbName("白芍");
////        pre2.setQuantity(1);
////        detailDomainList.add(pre2);
////        PrescriptionDetailDomain pre3=new PrescriptionDetailDomain();
////        pre3.setId(13);
////        pre3.setHerbName("当归");
////        pre3.setQuantity(2);
////        detailDomainList.add(pre3);
////        Collections.sort(detailDomainList, new MapComparator());
////        for (PrescriptionDetailDomain pre:detailDomainList
////             ) {
////            System.out.println(""+pre.getHerbName());
////        }
//        rr=new ReceiveResume();
//        String a=rr.getClass().toString();
//        String b[]=a.split(" ");
//        Class cla=null;
//        try {
//            cla=Class.forName(b[1]);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        //获取类名
//        System.out.println(cla.getSimpleName());
//        Field[] f=cla.getDeclaredFields();
//        //获取字段名
//        for(int i=0;i<f.length;i++){
//            System.out.println(f[i].getName());
//        }
//        System.out.println("OK！");
//    }
////    static class MapComparator implements Comparator<PrescriptionDetailDomain> {
////        @Override
////        public int compare(PrescriptionDetailDomain t1, PrescriptionDetailDomain t2) {
////           int pre1=t1.getQuantity();
////           int pre2=t2.getQuantity();
////            //逆序排列
////            if(pre1<pre2)
////                return 1;
////            else
////                return -1;
////        }
////    }
//}
