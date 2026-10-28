/**
 * 
 */
package com.fiserv.payment.connector.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
public class VersionController {

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.release.date}")
    private String appReleaseDate;

    @Value("${spring-boot.version}")
    private String appSpringBootVersion;

    /**
     * @return the appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion the appVersion to set
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return the appReleaseDate
     */
    public String getAppReleaseDate() {
        return appReleaseDate;
    }

    /**
     * @param appReleaseDate the appReleaseDate to set
     */
    public void setAppReleaseDate(String appReleaseDate) {
        this.appReleaseDate = appReleaseDate;
    }

    /**
     * @return the appSpringBootVersion
     */
    public String getAppSpringBootVersion() {
        return appSpringBootVersion;
    }

    /**
     * @param appSpringBootVersion the appSpringBootVersion to set
     */
    public void setAppSpringBootVersion(String appSpringBootVersion) {
        this.appSpringBootVersion = appSpringBootVersion;
    }

    @GetMapping("/version")
    public String getVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>").append("Application Version: ").append(getAppVersion()).append("\n").append("Release Date: ")
                .append(getAppReleaseDate()).append("\n").append("Powered by Spring Boot Version : ")
                .append(getAppSpringBootVersion()).append("</pre>");
        return sb.toString();
    }

}
