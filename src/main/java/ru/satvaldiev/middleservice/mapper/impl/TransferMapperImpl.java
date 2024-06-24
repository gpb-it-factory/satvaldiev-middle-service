package ru.satvaldiev.middleservice.mapper.impl;

import org.springframework.stereotype.Component;
import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;
import ru.satvaldiev.middleservice.mapper.TransferMapper;

@Component
public class TransferMapperImpl implements TransferMapper {
    @Override
    public TransferOutgoingDTO transferIncomingDTOtoTransferOutgoingDTO(TransferIncomingDTO transferIncomingDTO) {
        return new TransferOutgoingDTO(
                transferIncomingDTO.getFrom(),
                transferIncomingDTO.getTo(),
                transferIncomingDTO.getAmount()
        );
    }
}
