package br.com.guilda.service;

import br.com.guilda.model.PainelTaticoMissao;
import br.com.guilda.repository.PainelTaticoMissaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PainelTaticoMissaoServiceTest {

    @Mock
    private PainelTaticoMissaoRepository repository;

    @InjectMocks
    private PainelTaticoMissaoService service;

    @Test
    void deveBuscarTopMissoesDosUltimos15Dias() {
        PainelTaticoMissao missao1 = new PainelTaticoMissao();
        PainelTaticoMissao missao2 = new PainelTaticoMissao();

        when(repository.findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(any(OffsetDateTime.class)))
                .thenReturn(List.of(missao1, missao2));

        List<PainelTaticoMissao> resultado = service.buscarTopMissoesUltimos15Dias();

        assertThat(resultado).hasSize(2);
        verify(repository).findTop10ByUltimaAtualizacaoGreaterThanEqualOrderByIndiceProntidaoDesc(any(OffsetDateTime.class));
    }
}