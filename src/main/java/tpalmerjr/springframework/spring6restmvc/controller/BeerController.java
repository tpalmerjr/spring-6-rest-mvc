package tpalmerjr.springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import tpalmerjr.springframework.spring6restmvc.services.BeerService;

@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

}
