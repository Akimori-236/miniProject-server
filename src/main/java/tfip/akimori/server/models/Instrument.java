package tfip.akimori.server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instrument {
    private String brand;
    private String model;
    private String serial_number;
    private String store_name;
    private User user;
}
