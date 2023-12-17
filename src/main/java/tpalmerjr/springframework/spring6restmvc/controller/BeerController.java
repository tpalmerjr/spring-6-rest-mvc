package tpalmerjr.springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpalmerjr.springframework.spring6restmvc.model.Beer;
import tpalmerjr.springframework.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;

@Slf4j
//@AllArgsConstructor
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/v1/beer")
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Object> patchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Object> deleteById(@PathVariable("beerId") UUID beerId) {

        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Object> updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    @RequestMapping(method = RequestMethod.POST)
    @PostMapping(BEER_PATH)
    public ResponseEntity<Object> handlePost(@RequestBody Beer beer) {

        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

//    @RequestMapping(value =BEER_PATH, method = RequestMethod.GET)
    @GetMapping(BEER_PATH)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

//    @RequestMapping(value = BEER_PATH_ID, method = RequestMethod.GET)
    @GetMapping(BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("Get Beer by id - in controller");

        return beerService.getBeerById(beerId);
    }

}
