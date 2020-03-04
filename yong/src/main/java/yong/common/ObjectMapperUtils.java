package yong.common;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


public class ObjectMapperUtils {

    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ObjectMapperUtils() {
    }

    /**
     * 
     * entity에서 outClass Dto로 변환
     *
     * @since 2019. 12. 4.
     * @author kjh8877
     *
     * @param <D>
     * @param <T>
     * @param entity
     * @param outClass
     * @return
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    /**
     * 
     * entity List를 outClass Dto List로 변환
     *
     * @since 2019. 12. 4.
     * @author kjh8877
     *
     * @param <D>
     * @param <T>
     * @param entityList
     * @param outClass
     * @return
     */
    public static <D, T> List<D> mapList(final Collection<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> map(entity, outClass))
                .collect(Collectors.toList());
    }
}
