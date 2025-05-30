package desafio.cumbuca.controller;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;
import desafio.cumbuca.dtos.SaldoResponseDto;
import desafio.cumbuca.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Operation(summary = "Criar uma nova conta",
            description = "Criar uma nova conta a partir do dto de criação de conta e salva no banco de dados",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    // @ApiResponse(responseCode = "400", description = "Bad request. Invalid data sent")
            }
    )
    @PostMapping(path = "/criar", consumes = "application/json")
    public ResponseEntity<?> criarConta(@RequestBody CriarContaDto criarContaDto) {
        contaService.criarConta(criarContaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Autentica o usuário",
            description = "Autentica o usuário e retorna um token JWT para ser usado em futuras requisições",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JwtTokenDto.class))}),
                    @ApiResponse(responseCode = "500", description = "Error of generating JWT Token")
            })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> fazerLogin(@RequestBody AutenticarContaDto autenticarContaDto) {
        JwtTokenDto jwtToken = contaService.autenticarConta(autenticarContaDto);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }

    @Operation(summary = "Retorna o saldo",
            description = "Retorna o saldo total do usuário logado"
    )
    @GetMapping("/saldo")
    public ResponseEntity<SaldoResponseDto> consultarSaldo() {
        return ResponseEntity.status(HttpStatus.OK).body(contaService.consultarSaldo());
    }
}
