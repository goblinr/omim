#include "api_mark_container.hpp"
#include "api_mark_point.hpp"

#include "framework.hpp"


ApiUserMarkContainer::ApiUserMarkContainer(double layerDepth, Framework & framework)
  : UserMarkContainer(layerDepth, framework)
{
}

string ApiUserMarkContainer::GetTypeName() const
{
  return "api-result";
}

string ApiUserMarkContainer::GetActiveTypeName() const
{
  return "search-result-active";
}

UserMark * ApiUserMarkContainer::AllocateUserMark(const m2::PointD & ptOrg)
{
  return new ApiMarkPoint(ptOrg, this);
}
