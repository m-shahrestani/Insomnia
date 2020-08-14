package Http;

/**
 * A class to drive Http program.
 *
 * @author Mohammadreza Shahrestani
 * @version 1.0
 */
public class HttpMain {
    public static void main(String[] args) throws Exception {
//        test requests
//        "https://www.google.com/"
//        "http://apapi.haditabatabaei.ir/"
//        "http://apapi.haditabatabaei.ir/tests/post/formdata"
//        http://apapi.haditabatabaei.ir/tests/follow/portal.aut.ac.ir
//        String requestHeader = "name:hadi;lastName:tabatabaei";
//        String formData = "firstName=hadi&lastName=Tabatabaei";
//        String[] arg = new String[]{ "http://apapi.haditabatabaei.ir/tests/post/formdata", "-M", "POST",
//                "-H", requestHeader, "-d", formData, "-i", "-S"};
//        String[] arg = new String[]{ "http://portal.aut.ac.ir", "-M",
//                "-H", requestHeader, "-i", "-O", "-S", "-f"};
//        String[] arg = new String[]{ "http://www.google.com", "-M",
//                "-H", "name:hadi;lastName:tabatabaei", "-i", "-O", "-k", "-f"};
//      String[] arg =  new String[]{"apapi.haditabatabaei.ir/tests/get/buffer/pic","-O","picture.png" };
//       String[] arg =  new String[]{"fire" , "1" , "7"};
//       String[] arg =  new String[]{"list"};
//        String[] arg =  new String[]{"www.portal.aut.ir"};
        Command.Command(args);
    }
}