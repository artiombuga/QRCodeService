package qrcodeapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import qrcodeapi.service.BaseService;

@Controller
@RequestMapping("api")
public class BaseController {
    private final BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping("health")
    @ResponseStatus(HttpStatus.OK)
    public void getHealth() {
    }

    @GetMapping("qrcode")
    public ResponseEntity<?> getImage(@RequestParam(required = false) String contents,
                                      @RequestParam(required = false, defaultValue = "250") int size,
                                      @RequestParam(required = false, defaultValue = "png") String type,
                                      @RequestParam(required = false, defaultValue = "L") String correction) {
        return baseService.getQRCode(contents, size, type, correction);
    }

}
