package com.example.study.service;

import com.example.study.interpace.CrudInterface;
import com.example.study.model.entity.Item;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.PartnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class ItemApiLogicService extends BaseService<ItemApiRequest, ItemApiResponse, Item> {

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {

        ItemApiRequest body = request.getData();

        Item item = Item.builder()
                .status(body.getStatus())
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now())
                .partner(partnerRepository.getOne(body.getPartnerId()))
                .build();

        Item newItem = baseRepository.save(item);
        return response(newItem);
    }

    @Override
    public Header<ItemApiResponse> read(Long id) {
        log.info("## ITEM API SERVICE ID {}", id);
        return baseRepository.findById(id)
                .map(this::response)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<ItemApiResponse> update(Header<ItemApiRequest> request) {

        ItemApiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                    .map(entityItem -> {
                        entityItem
                                .setStatus(body.getStatus())
                                .setName(body.getName())
                                .setTitle(body.getTitle())
                                .setContent(body.getContent())
                                .setPrice(body.getPrice())
                                .setBrandName(body.getBrandName())
                                .setRegisteredAt(body.getRegisteredAt())
                                .setUnregisteredAt(body.getUnregisteredAt())
                                ;
                        return entityItem;
                    })
                    .map(newEntityItem -> baseRepository.save(newEntityItem))
                    .map(this::response)
                    .orElseGet(() -> Header.ERROR("없데이트!"));
    }

    @Override
    public Header delete(Long id) {

        Optional<Item> optional = baseRepository.findById(id);

        return optional.map
                (item -> {
                    baseRepository.delete(item);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("삭제 할 데이터 없음!"));
    }

    private Header<ItemApiResponse> response(Item item){

        //String statusTitle = item.getStatus().getTitle();

        ItemApiResponse data = ItemApiResponse.builder()
                .id(item.getId())
                .status(item.getStatus())
                .name(item.getName())
                .title(item.getTitle())
                .content(item.getContent())
                .price(item.getPrice())
                .brandName(item.getBrandName())
                .registeredAt(item.getRegisteredAt())
                .unregisteredAt(item.getUnregisteredAt())
                .partnerId(item.getPartner().getId())
                .build();

        return Header.OK(data);
    }
}
