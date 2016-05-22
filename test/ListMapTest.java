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
//        List<PrescriptionDetailDomain> detailDomainList=new ArrayList<>();
//        PrescriptionDetailDomain pre1=new PrescriptionDetailDomain();
//        pre1.setId(10);
//        pre1.setHerbName("陈皮");
//        pre1.setQuantity(3);
//        detailDomainList.add(pre1);
//        PrescriptionDetailDomain pre2=new PrescriptionDetailDomain();
//        pre2.setId(12);
//        pre2.setHerbName("白芍");
//        pre2.setQuantity(1);
//        detailDomainList.add(pre2);
//        PrescriptionDetailDomain pre3=new PrescriptionDetailDomain();
//        pre3.setId(13);
//        pre3.setHerbName("当归");
//        pre3.setQuantity(2);
//        detailDomainList.add(pre3);
//        Collections.sort(detailDomainList, new MapComparator());
//        for (PrescriptionDetailDomain pre:detailDomainList
//             ) {
//            System.out.println(""+pre.getHerbName());
//        }
//        System.out.println("OK！");
//    }
//    static class MapComparator implements Comparator<PrescriptionDetailDomain> {
//        @Override
//        public int compare(PrescriptionDetailDomain t1, PrescriptionDetailDomain t2) {
//           int pre1=t1.getQuantity();
//           int pre2=t2.getQuantity();
//            //逆序排列
//            if(pre1<pre2)
//                return 1;
//            else
//                return -1;
//        }
//    }
//}
