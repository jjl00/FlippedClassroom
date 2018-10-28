//package io.flippedclassroom.server.new_flippedclassroom;
//
//import org.junit.Test;
//
//
///**
// * 描述:
// * 测试Java se
// *
// * @author HASEE
// * @create 2018-09-22 15:34
// */
//public class mainTest {
//    @Test
//    public void test(){
//        Thread thread= new Thread (()->{
//            try{
//                for (int i=0;i<5;i++){
//                    try {
//                        Thread.sleep(500);
//                        System.out.println(i);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        throw new InterruptedException();
//                    }
//                }
//            }catch (InterruptedException e){
//                e.printStackTrace();
//                System.out.println("线程中断");
//            }
//
//        });
//
//        thread.interrupt();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("主程序运行完成");
//    }
//}