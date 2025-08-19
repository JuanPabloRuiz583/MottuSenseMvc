package br.com.fiap.MottuSenseMvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Placa não pode estar em branco")
    @Size(max = 10, message = "Placa não pode exceder 10 caracteres")
    private String placa;

    @NotBlank(message = "Modelo não pode estar em branco")
    @Size(max = 50, message = "Modelo não pode exceder 50 caracteres")
    private String modelo;

    @NotBlank(message = "Número do Chassi não pode estar em branco")
    @Size(max = 17, message = "Número do Chassi não pode exceder 17 caracteres")
    private String numeroChassi;

    @NotNull(message = "Status não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private StatusMoto status;

   // @ManyToOne
    //@JoinColumn(name = "patio_id")
    //private Patio patio;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    //@OneToMany(mappedBy = "moto", cascade = CascadeType.REMOVE, orphanRemoval = true)
    //private List<SensorLocalizacao> sensores;
}
