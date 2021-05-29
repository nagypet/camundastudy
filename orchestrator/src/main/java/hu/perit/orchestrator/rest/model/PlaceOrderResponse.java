package hu.perit.orchestrator.rest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceOrderResponse
{
    String processInstanceId;
}
