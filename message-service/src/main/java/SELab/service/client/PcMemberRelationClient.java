package SELab.service.client;

import SELab.domain.PCMemberRelation;

import java.util.List;

/**
 * @author xywu
 * @date 2020/11/03
 */
public interface PcMemberRelationClient {

    List<String>  getInvitationResultMessagesByChairName(String chairName);

    List<String> getArticleResultMessagesByAuthorName(String authorName);
}
