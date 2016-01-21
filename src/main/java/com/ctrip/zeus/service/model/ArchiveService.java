package com.ctrip.zeus.service.model;

import com.ctrip.zeus.model.entity.Group;
import com.ctrip.zeus.model.entity.Archive;
import com.ctrip.zeus.model.entity.Slb;
import com.ctrip.zeus.model.entity.VirtualServer;

import java.util.List;

/**
 * @author:xingchaowang
 * @date: 3/14/2015.
 */
public interface ArchiveService {

    Slb getSlb(Long slbId, int version) throws Exception;

    Group getGroup(Long groupId, int version) throws Exception;

    VirtualServer getVirtualServer(Long vsId, int version) throws Exception;


    List<Group> listGroups(IdVersion[] keys) throws Exception;

    List<VirtualServer> listVirtualServers(IdVersion[] keys) throws Exception;

    List<Slb> listSlbs(IdVersion[] keys) throws Exception;


    Archive getSlbArchive(Long slbId, int version) throws Exception;

    Archive getGroupArchive(Long groupId, int version) throws Exception;

    Archive getVsArchive(Long vsId, int version) throws Exception;
}
