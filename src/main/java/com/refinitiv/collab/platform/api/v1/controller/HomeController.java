package com.refinitiv.collab.platform.api.v1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
@Api(description = "for test")
@AllArgsConstructor
public class HomeController {
    @ApiOperation(value = "output pang if everything good", notes = "nothing here")
    @RequestMapping(value = {"/ping"}, method = RequestMethod.GET)
    public String ping() {
        return "pang!";
    }
}
