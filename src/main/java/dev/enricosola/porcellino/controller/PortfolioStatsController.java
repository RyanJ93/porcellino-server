package dev.enricosola.porcellino.controller;

import dev.enricosola.porcellino.response.PortfolioCompositionStatsResponse;
import dev.enricosola.porcellino.response.portfolio.PortfolioStatsResponse;
import dev.enricosola.porcellino.dto.PortfolioCompositionStatsDTO;
import dev.enricosola.porcellino.service.PortfolioStatsService;
import dev.enricosola.porcellino.service.PortfolioService;
import dev.enricosola.porcellino.dto.PortfolioStatsDTO;
import dev.enricosola.porcellino.entity.Portfolio;
import org.springframework.web.bind.annotation.*;
import dev.enricosola.porcellino.util.DateUtils;
import org.springframework.http.ResponseEntity;
import java.util.Date;

@RestController
@RequestMapping("/api/portfolio/{portfolioId}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PortfolioStatsController {
    private final PortfolioStatsService portfolioStatsService;
    private final PortfolioService portfolioService;

    public PortfolioStatsController(PortfolioStatsService portfolioStatsService, PortfolioService portfolioService){
        this.portfolioStatsService = portfolioStatsService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/stats")
    public ResponseEntity<PortfolioStatsResponse> stats(
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @PathVariable("portfolioId") String portfolioId
    ){
        Date startDate = DateUtils.parse(startDateStr), endDate = DateUtils.parse(endDateStr);
        Portfolio portfolio = this.portfolioService.getById(Integer.parseInt(portfolioId));
        PortfolioStatsDTO stats;
        if ( startDate != null && endDate != null ){
            stats = this.portfolioStatsService.computeStats(portfolio, startDate, endDate);
        }else{
            stats = this.portfolioStatsService.computeStats(portfolio);
        }
        return ResponseEntity.ok(new PortfolioStatsResponse(stats));
    }

    @GetMapping("/composition-stats")
    public ResponseEntity<PortfolioCompositionStatsResponse> compositionStats(
        @RequestParam(name = "startDate", required = false) String startDateStr,
        @RequestParam(name = "endDate", required = false) String endDateStr,
        @PathVariable("portfolioId") String portfolioId
    ){
        Date startDate = DateUtils.parse(startDateStr), endDate = DateUtils.parse(endDateStr);
        Portfolio portfolio = this.portfolioService.getById(Integer.parseInt(portfolioId));
        if ( endDate == null ){
            endDate = new Date();
        }
        if ( startDate == null ){
            startDate = DateUtils.getFirstOfMonthDate(endDate);
        }
        PortfolioCompositionStatsDTO compositionStats = this.portfolioStatsService.computePortfolioCompositionStats(portfolio, startDate, endDate);
        PortfolioStatsDTO stats = this.portfolioStatsService.computeStats(portfolio, endDate);
        return ResponseEntity.ok(new PortfolioCompositionStatsResponse(compositionStats, stats));
    }
}
