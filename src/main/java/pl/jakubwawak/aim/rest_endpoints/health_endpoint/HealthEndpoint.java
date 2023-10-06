/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
*/
package pl.jakubwawak.aim.rest_endpoints.health_endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Object for creating health endpoint
 */
@RestController
public class HealthEndpoint {

    /**
     * /rest/health endpoint
     * @return HealthResponse
     */
    @GetMapping("/rest/health")
    public HealthResponse rest_health(){
        return new HealthResponse();
    }
}
