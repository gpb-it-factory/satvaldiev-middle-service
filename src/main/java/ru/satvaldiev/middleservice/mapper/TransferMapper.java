package ru.satvaldiev.middleservice.mapper;

import ru.satvaldiev.middleservice.dto.TransferIncomingDTO;
import ru.satvaldiev.middleservice.dto.TransferOutgoingDTO;

public interface TransferMapper {
    TransferOutgoingDTO transferIncomingDTOtoTransferOutgoingDTO(TransferIncomingDTO transferIncomingDTO);
}
