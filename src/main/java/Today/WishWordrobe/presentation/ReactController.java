package Today.WishWordrobe.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
public class ReactController {
    @GetMapping("/api")
    public String hello(){
        Date now = new Date();
        String strNow1= now.toString();
        return "안녕하세요. 현재 "+strNow1 +"입니다.\n";
    }

}
