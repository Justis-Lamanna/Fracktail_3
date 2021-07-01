package com.github.milomarten.fracktail3.modules.cc;

import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.spring.command.annotation.*;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class CrowdControl {
    @Autowired
    private CrowdControlServer server;

    @Command
    @OnExceptionRespond("Error starting Crowd Control server. Please check the logs.")
    @Role("owner")
    public String ccstart(@InjectPlace Place origin) throws IOException {
        if(server.isRunning()) {
            return "Server is already running.";
        } else {
            server.getSubscriptions()
                    .next()
                    .flatMap(i -> origin.sendMessage("Connection received! Start the Chaos!"))
                    .subscribe();
            server.start();
            if (server.isRunning()) {
                return "Listening for incoming connections...";
            } else {
                return "Unable to start server. Check logs for more details.";
            }
        }
    }

    @Command
    public String redemptions() {
        String redemptions = EnumUtils.getEnumList(CCRedemption.class)
                .stream()
                .map(CCRedemption::getEffect)
                .collect(Collectors.joining(", "));
        return "Possible redemptions are: " + redemptions;
    }

    @Command
    public String redeem(@InjectPerson Person redeemer, @Parameter(0) String redemption) {
        Optional<CCRedemption> r = EnumUtils.getEnumList(CCRedemption.class)
                .stream()
                .filter(c -> c.getEffect().equalsIgnoreCase(redemption))
                .findFirst();
        if(r.isEmpty()) {
            return "That is not a real redemption.";
        } else {
            CCRequestModel requestModel = new CCRequestModel(r.get(), redeemer.getName(), CCRequestType.START);
            if(server.isRunning()) {
                RedemptionResult result = server.send(requestModel);
                switch (result.getStatus()) {
                    case FAIL:
                        return "Redemption failed: " + result.getReason();
                    case NONE:
                        return "Could not redeem prize, since game is not started.";
                    default:
                        return "Redemption succeeded!";
                }
            } else {
                return "Crowd Control is not enabled at this time.";
            }
        }
    }

    @Command
    @OnExceptionRespond("Error stopping Crowd Control server. Please check the logs.")
    @Role("owner")
    public String ccstop() throws IOException {
        if(server.isRunning()) {
            server.stop();
            if(server.isRunning()) {
                return "Error stopping server.";
            } else {
                return "No longer listening for incoming connections";
            }
        } else {
            return "Server is not running";
        }
    }
}
